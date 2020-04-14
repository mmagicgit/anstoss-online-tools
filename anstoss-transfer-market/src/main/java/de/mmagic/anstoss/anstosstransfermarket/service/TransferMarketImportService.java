package de.mmagic.anstoss.anstosstransfermarket.service;

import de.mmagic.anstoss.anstosstransfermarket.model.AawCategory;
import de.mmagic.anstoss.anstosstransfermarket.model.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TransferMarketImportService {

    private static final List<String> positions = Arrays.asList("LIB", "MD", "LV", "RV", "ZM", "RM", "LM", "ST");

    @Value("${ANSTOSS_USER}")
    private String user;
    @Value("${ANSTOSS_PW}")
    private String password;

    public List<Player> search() {
        AnstossOnlineHttp http = new AnstossOnlineHttp();
        http.login(user, password);

        String positionQueryParameters = positions.stream().map(p -> "idealpos[]=" + p).collect(Collectors.joining(";"));

        String searchUrl = String.format("content/getContent.php?dyn=transfers/spielersuche;erg=1;;%s;wettbewerb_id=&land_id=&genauigkeit=1&staerke_min=3&staerke_max=&alter_min=&alter_max=24&spielerboerse=1", positionQueryParameters);
        String searchResponse = http.get(searchUrl);
        Document searchDocument = Jsoup.parse(searchResponse, StandardCharsets.ISO_8859_1.name());

        List<String> pageLinks = searchDocument.select(".navigation > a[href]").stream().map(element -> element.attr("href")).collect(Collectors.toList());
        if (pageLinks.isEmpty()) {
            pageLinks.add(searchUrl);
        }

        List<Player> players = new ArrayList<>();
        for (String pageLink : pageLinks) {
            String pageResponse = http.get(pageLink);
            Document pageDocument = Jsoup.parse(pageResponse, StandardCharsets.ISO_8859_1.name());

            Elements rows = pageDocument.select("table.daten_tabelle tr:gt(0)");
            for (Element row : rows) {
                String position = row.select("td:eq(0)").text();
                String name = row.select("td:eq(1)").text();
                String power = row.select("td:eq(2)").text();
                String age = row.select("td:eq(4)").text();
                String country = row.select("td:eq(5) img").attr("title");
                String cash = row.select("td:eq(7)").text().replaceAll("\\.", "");
                String days = row.select("td:eq(8)").text();
                String playerLink = row.select("[href*=spieler]").attr("href");
                String playerId = playerLink.replace("?do=spieler;spieler_id=", "").replace("#", "");
                String aawLink = "content/getContent.php?dyn=transfers/aaw;spieler_id=" + playerId;
                String aawResponse = http.get(aawLink);
                Document aawDocument = Jsoup.parse(aawResponse, StandardCharsets.ISO_8859_1.name());
                Elements aaws = aawDocument.select("tr.hide");
                aaws.remove(aaws.last());

                Map<String, List<Integer>> multiMap = new HashMap<>();
                List.of(AawCategory.values()).forEach(aawCategory -> multiMap.put(aawCategory.name(), new ArrayList<>()));

                aaws.stream().map(tr -> tr.select("td:lt(6)").eachText())
                        .forEach(values -> addDataToMultiMap(values, multiMap));
                players.add(new Player(Integer.valueOf(playerId), name, Integer.valueOf(age), Double.valueOf(power.replace(",", ".")), position, country, Long.valueOf(cash), Integer.valueOf(days), multiMap));
            }
        }
        return players;
    }

    private void addDataToMultiMap(List<String> values, Map<String, List<Integer>> multiMap) {
        IntStream.range(0, values.size()).forEach(i -> {
            Integer percentValue = Integer.valueOf(values.get(i).replace("%", "").trim());
            multiMap.get(AawCategory.values()[i].name()).add(percentValue);
        });
    }
}
