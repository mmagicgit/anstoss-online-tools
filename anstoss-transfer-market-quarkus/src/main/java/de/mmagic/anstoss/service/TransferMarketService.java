package de.mmagic.anstoss.service;

import de.mmagic.anstoss.model.Player;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.enterprise.context.ApplicationScoped;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransferMarketService {

    private static final List<String> positions = Arrays.asList("LIB", "MD", "LV", "RV", "ZM", "RM", "LM", "ST");

    @ConfigProperty(name="ANSTOSS_USER")
    String user;

    @ConfigProperty(name = "ANSTOSS_PW")
    String password;

    public List<Player> search() {
        AnstossOnlineHttp http = new AnstossOnlineHttp();
        http.login(user, password);

        String positionQueryParameters = positions.stream().map(p -> "idealpos[]=" + p).collect(Collectors.joining(";"));

        String searchUrl = String.format("content/getContent.php?dyn=transfers/spielersuche;erg=1;;%s;wettbewerb_id=&land_id=&genauigkeit=1&staerke_min=&staerke_max=&alter_min=&alter_max=24&spielerboerse=1", positionQueryParameters);
        HttpResponse<String> searchResponse = http.get(searchUrl);
        Document searchDocument = Jsoup.parse(searchResponse.body(), StandardCharsets.ISO_8859_1.name());

        List<String> pageLinks = searchDocument.select(".navigation > a[href]").stream().map(element -> element.attr("href")).collect(Collectors.toList());
        if (pageLinks.isEmpty()) {
            pageLinks.add(searchUrl);
        }

        List<Player> players = new ArrayList<>();
        for (String pageLink : pageLinks) {
            HttpResponse<String> pageResponse = http.get(pageLink);
            Document pageDocument = Jsoup.parse(pageResponse.body(), StandardCharsets.ISO_8859_1.name());

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
                HttpResponse<String> aawResponse = http.get(aawLink);
                Document aawDocument = Jsoup.parse(aawResponse.body(), StandardCharsets.ISO_8859_1.name());
                Elements aaws = aawDocument.select("tr.hide");
                aaws.remove(aaws.last());

                aaws.stream()
                        .map(tr -> tr.select("td:lt(5)").eachText())
                        .flatMap(Collection::stream)
                        .map(percent -> percent.replace("%", "").trim())
                        .filter(percent -> Integer.parseInt(percent) >= 15)
                        .findFirst().ifPresent(s -> players.add(new Player(Integer.valueOf(playerId), name, Integer.valueOf(age), Double.valueOf(power.replace(",", ".")), position, country, Long.valueOf(cash), Integer.valueOf(days))));
            }
        }
        return players;
    }
}
