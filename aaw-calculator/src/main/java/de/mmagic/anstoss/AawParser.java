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
import java.util.function.Consumer;

class AawParser {

    List<Player> readFiles(String... path) throws IOException {
        List<Player> playerList = new ArrayList<>();

        for (String file : path) {
            if (file.endsWith(".json")) {
                Gson gson = new Gson();
                JsonPlayer[] jsonPlayerArray = gson.fromJson(new String(Files.readAllBytes(Paths.get(file)), Charsets.ISO_8859_1), JsonPlayer[].class);

                Arrays.asList(jsonPlayerArray).forEach(jsonPlayer -> {
                    Player player = findOrCreatePlayer(playerList, jsonPlayer.getName());
                    int percent = Integer.parseInt(jsonPlayer.getSchnelligkeit()[0].replaceAll("%", "").trim());
                    player.data.put("Schnelligkeit", new Aaw(percent, jsonPlayer.getSchnelligkeit()[1]));
                    percent = Integer.parseInt(jsonPlayer.getZweikampf()[0].replaceAll("%", "").trim());
                    player.data.put("Zweikampf", new Aaw(percent, jsonPlayer.getZweikampf()[1]));
                    percent = Integer.parseInt(jsonPlayer.getKopfball()[0].replaceAll("%", "").trim());
                    player.data.put("Kopfball", new Aaw(percent, jsonPlayer.getKopfball()[1]));
                    percent = Integer.parseInt(jsonPlayer.getSchusskraft()[0].replaceAll("%", "").trim());
                    player.data.put("Schusskraft", new Aaw(percent, jsonPlayer.getSchusskraft()[1]));
                    percent = Integer.parseInt(jsonPlayer.getSchussgenauigkeit()[0].replaceAll("%", "").trim());
                    player.data.put("Schussgenauigkeit", new Aaw(percent, jsonPlayer.getSchussgenauigkeit()[1]));
                    percent = Integer.parseInt(jsonPlayer.getTechnik()[0].replaceAll("%", "").trim());
                    player.data.put("Technik", new Aaw(percent, jsonPlayer.getTechnik()[1]));
                    percent = Integer.parseInt(jsonPlayer.getSpielintelligenz()[0].replaceAll("%", "").trim());
                    player.data.put("Spielintelligenz", new Aaw(percent, jsonPlayer.getSpielintelligenz()[1]));
                    playerList.add(player);
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
                playerList.add(player);
            });
        }
        return playerList;
    }

    private Player findOrCreatePlayer(List<Player> playerList, String name) {
        return playerList.stream().filter(player -> player.name.equals(name)).findFirst().orElseGet(() -> new Player(name));
    }

}
