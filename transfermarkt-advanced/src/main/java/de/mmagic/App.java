package de.mmagic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    private static final List<String> positions = Arrays.asList("LIB", "MD", "LV", "ZM", "RM", "ST");

    public static void main(String[] args) throws Exception {
        AnstossOnlineHttp http = new AnstossOnlineHttp();
        http.login(args[0], args[1]);

        String positionQueryParameters = positions.stream().map(p -> "idealpos[]=" + p).collect(Collectors.joining(";"));

        String searchUrl = "content/getContent.php?dyn=transfers/spielersuche;erg=1;;" + positionQueryParameters + ";wettbewerb_id=&land_id=&genauigkeit=1&staerke_min=&staerke_max=&alter_min=&alter_max=24&spielerboerse=1";
        HttpResponse<String> searchResponse = http.get(searchUrl);
        Document searchDocument = Jsoup.parse(searchResponse.body(), StandardCharsets.ISO_8859_1.name());

        List<String> pageLinks = searchDocument.select(".navigation > a[href]").stream().map(element -> element.attr("href")).collect(Collectors.toList());
        if (pageLinks.isEmpty()) {
            pageLinks.add(searchUrl);
        }

        for (String pageLink : pageLinks) {
            HttpResponse<String> pageResponse = http.get(pageLink);
            Document pageDocument = Jsoup.parse(pageResponse.body(), StandardCharsets.ISO_8859_1.name());

            Elements table = pageDocument.select("table.daten_tabelle [href*=spieler]");
            for (Element playerElement : table) {
                String playerLink = playerElement.attr("href");
                String playerId = playerLink.replace("?do=spieler;spieler_id=", "").replace("#", "");
                String aawLink = "content/getContent.php?dyn=transfers/aaw;spieler_id=" + playerId;
                HttpResponse<String> aawResponse = http.get(aawLink);
                Document aawDocument = Jsoup.parse(aawResponse.body(), StandardCharsets.ISO_8859_1.name());
                Elements aaws = aawDocument.select("tr.hide");

                aaws.stream()
                        .map(tr -> tr.select("td:eq(4)").eachText())
                        .flatMap(Collection::stream)
                        .map(percent -> percent.replace("%", "").trim())
                        .filter(percent -> percent.length() > 1)
                        .findFirst().ifPresent(s -> System.out.println(AnstossOnlineHttp.BASE_URL + playerLink));
            }
        }

    }

}
