package de.mmagic.anstoss;

import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class AawParser {

    List<Player> readFiles(List<String> pathList) throws IOException {
        List<Player> playerList = new ArrayList<>();

        for (String file : pathList) {
            Document documentForCharset = Jsoup.parse(new File(file), StandardCharsets.UTF_8.name());
            String charset = documentForCharset.getElementsByTag("meta")
                    .get(0).attr("content")
                    .replaceAll("text/html;", "").replaceAll("charset=", "").trim();

            Document document = Jsoup.parse(new File(file), charset);
            Elements tbodyElements = document.getElementsByTag("tbody");
            tbodyElements.remove(0);
            tbodyElements.remove(tbodyElements.size() - 1);
            tbodyElements.remove(tbodyElements.size() - 1);
            tbodyElements.remove(tbodyElements.size() - 1);

            List<String> playerNames = new ArrayList<>();
            tbodyElements.forEach(element -> {
                String name = element.getElementsByTag("a").text();
                String position = element.getElementsByTag("td").get(0).text().trim();
                String strength = Iterables.getLast(element.getElementsByTag("td")).text().trim();
                //System.out.println("\n" + name);
                Player player = findOrCreatePlayer(playerList, name, position);
                player.strength = new BigDecimal(strength);

                Elements line = element.getElementsByTag("tr");
                line.remove(0);
                line.remove(0);
                line.remove(0);
                line.remove(line.size() - 1);
                line.forEach((Element x) -> {
                    String text = x.getElementsByTag("th").text();
                    Elements td = x.getElementsByTag("td");
                    String gesamt = td.get(7).text();
                    String upOrDown = td.get(8).getElementsByTag("img").attr("alt");
                    //System.out.println(text + " " + gesamt + " " + upOrDown);
                    int gesamtAsInteger = Integer.parseInt(gesamt.replaceAll("%", "").trim());
                    player.data.put(text, new Aaw(gesamtAsInteger, upOrDown));
                });
                addPlayerIfNew(playerList, name, player);
                playerNames.add(player.name);
            });
            List<Player> playersToRemove = playerList.stream()
                    .filter(player -> !playerNames.contains(player.name))
                    .collect(Collectors.toList());
            playerList.removeAll(playersToRemove);
        }

        Ordering<String> explicit = Ordering.explicit("TW", "MD", "RV", "LV", "LV RV", "LIB", "LM", "LM RM", "RM", "ZM", "ST");
        return playerList.stream().sorted((player, player2) -> {
            int compare = explicit.compare(player.position, player2.position);
            if (compare != 0) {
                return compare;
            }
            return player.strength.compareTo(player2.strength) * -1;
        }).collect(Collectors.toList());
    }

    private void addPlayerIfNew(List<Player> playerList, String name, Player player) {
        if (playerList.stream().noneMatch(p -> name.equals(p.name))) {
            playerList.add(player);
        }
    }

    private Player findOrCreatePlayer(List<Player> playerList, String name, String position) {
        return playerList.stream().filter(player -> player.name.equals(name)).findFirst().orElseGet(() -> new Player(name, position));
    }

}
