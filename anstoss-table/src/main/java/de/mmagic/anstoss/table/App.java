package de.mmagic.anstoss.table;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {
        Map<String, TableRow> clubs = clubsToInclude();

        AnstossOnlineHttp anstossOnlineHttp = new AnstossOnlineHttp();
        anstossOnlineHttp.login(args[0], args[1]);
        fetchData(anstossOnlineHttp, clubs);

        List<TableRow> rows = clubs.entrySet().stream()
                .sorted(new TableEntryComparator())
                .map(Entry::getValue)
                .collect(Collectors.toList());

        for (int i = 0; i < rows.size(); i++) {
            TableRow currentRow = rows.get(i);
            System.out.println((i + 1) + ". " + currentRow.name + " " + currentRow.games + " " + currentRow.goals + ":" + currentRow.goals2 + " " + currentRow.points);
        }
    }

    private static void fetchData(AnstossOnlineHttp anstossOnlineHttp, Map<String, TableRow> clubs) {
        for (int day = 1; day < 35; day++) {
            HttpResponse<String> stringHttpResponse = anstossOnlineHttp.get("?do=land;land_id=168;wettbewerb_st_id=240;spieltag_nr=" + day + ";start_jahr=2019");
            Document spieltag = Jsoup.parse(stringHttpResponse.body());

            Element table = spieltag.selectFirst("table.daten_tabelle");
            Elements row = table.select("tr:lt(10):gt(0)");
            Elements as = row.select("a[title]");
            List<String> ids = as.stream().map(element -> element.attr("href")
                    .replaceAll("#", "").replaceAll("\\?do=verein;verein_id=", ""))
                    .collect(Collectors.toList());
            List<String> results = row.select(".spiel_ergebnis").stream().map(element -> element.text().trim()).collect(Collectors.toList());

            for (int i = 0; i < results.size(); i++) {
                String currentResult = results.get(i);
                if ("- : -".equals(currentResult)) {
                    break;
                }

                String home = ids.get(i * 2);
                String guest = ids.get(i * 2 + 1);
                if (!clubs.containsKey(home) || !clubs.containsKey(guest)) {
                    continue;
                }
                TableRow homeClub = clubs.get(home);
                TableRow guestClub = clubs.get(guest);
                homeClub.games += 1;
                guestClub.games += 1;

                String[] result = currentResult.split(":");
                int homeGoals = Integer.parseInt(result[0].trim());
                int guestGoals = Integer.parseInt(result[1].trim());
                homeClub.goals += homeGoals;
                homeClub.goals2 += guestGoals;
                guestClub.goals += guestGoals;
                guestClub.goals2 += homeGoals;

                if (homeGoals == guestGoals) {
                    homeClub.points += 1;
                    guestClub.points += 1;
                }
                if (homeGoals > guestGoals) {
                    homeClub.points += 3;
                }
                if (homeGoals < guestGoals) {
                    guestClub.points += 3;
                }
            }
        }
    }

    private static Map<String, TableRow> clubsToInclude() {
        return Map.of(
                "5045", new TableRow("Disportivo Aves"),
                "5051", new TableRow("FV Famalicao"),
                "5055", new TableRow("DVD National"),
                "5059", new TableRow("Vitóría FV Setúbal"),
                "5050", new TableRow("FV Pacos de Ferreira"),
                "5043", new TableRow("National Neval"),
                "194", new TableRow("DVD Santa Clara")
        );
    }
}
