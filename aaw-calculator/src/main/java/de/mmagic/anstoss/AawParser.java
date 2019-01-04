package de.mmagic.anstoss;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class AawParser {

    List<Player> readFiles(String... path) throws IOException {
        List<Player> playerList = new ArrayList<>();

        for (String file : path) {
            if (file.endsWith(".json")) {
                Gson gson = new Gson();
                JsonPlayer[] jsonPlayerArray = gson.fromJson(new String(Files.readAllBytes(Paths.get(file)), Charsets.ISO_8859_1), JsonPlayer[].class);
                Arrays.asList(jsonPlayerArray).forEach(jsonPlayer -> {
                    String name = jsonPlayer.getName();
                    Player player = findOrCreatePlayer(playerList, name);
                    addData(player, jsonPlayer.getSchnelligkeit(), "Schnelligkeit");
                    addData(player, jsonPlayer.getZweikampf(), "Zweikampf");
                    addData(player, jsonPlayer.getKopfball(), "Kopfball");
                    addData(player, jsonPlayer.getSchusskraft(), "Schusskraft");
                    addData(player, jsonPlayer.getSchusskraft(), "Schussgenauigkeit");
                    addData(player, jsonPlayer.getTechnik(), "Technik");
                    addData(player, jsonPlayer.getSpielintelligenz(), "Spielintelligenz");
                    addPlayerIfNew(playerList, name, player);
                });
                continue;
            }

            Document document = Jsoup.parse(new File(file), StandardCharsets.ISO_8859_1.name());
            Elements tbodyElements = document.getElementsByTag("tbody");
            tbodyElements.remove(0);
            tbodyElements.remove(0);
            tbodyElements.remove(0);
            tbodyElements.remove(0);
            tbodyElements.remove(tbodyElements.size() - 1);
            tbodyElements.remove(tbodyElements.size() - 1);
            tbodyElements.remove(tbodyElements.size() - 1);

            tbodyElements.forEach(element -> {
                String name = element.getElementsByTag("a").text();
                System.out.println("\n" + name);
                Player player = findOrCreatePlayer(playerList, name);

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
                    System.out.println(text + " " + gesamt + " " + upOrDown);
                    int gesamtAsInteger = Integer.valueOf(gesamt.replaceAll("%", "").trim());
                    player.data.put(text, new Aaw(gesamtAsInteger, upOrDown));
                });
                addPlayerIfNew(playerList, name, player);
            });
        }
        return playerList;
    }

    private void addPlayerIfNew(List<Player> playerList, String name, Player player) {
        if (playerList.stream().noneMatch(p -> name.equals(p.name))) {
            playerList.add(player);
        }
    }

    private void addData(Player player, String[] data, String key) {
        int percent2 = Integer.parseInt(data[0].replaceAll("%", "").trim());
        player.data.put(key, new Aaw(percent2, data[1]));
    }

    private Player findOrCreatePlayer(List<Player> playerList, String name) {
        return playerList.stream().filter(player -> player.name.equals(name)).findFirst().orElseGet(() -> new Player(name));
    }

}
