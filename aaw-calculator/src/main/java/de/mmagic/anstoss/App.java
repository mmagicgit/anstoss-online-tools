package de.mmagic.anstoss;

import com.google.common.base.Strings;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;

import java.io.IOException;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        String path2 = "/home/ms/Entwicklung/Git/anstoss-online-tools/aaw-calculator/src/main/resources/2018_1.html";
        String path3 = "/home/ms/Entwicklung/Git/anstoss-online-tools/aaw-calculator/src/main/resources/2018_2.html";
        String path4 = "/home/ms/Entwicklung/Git/anstoss-online-tools/aaw-calculator/src/main/resources/2019_1.html";
        String path5 = "/home/ms/Entwicklung/Git/anstoss-online-tools/aaw-calculator/src/main/resources/2019_2.html";
        String path6 = "/home/ms/Entwicklung/Git/anstoss-online-tools/aaw-calculator/src/main/resources/2020_1.html";

        Ordering<String> ordering = Ordering.explicit(
                "Schnelligkeit", "Zweikampf", "Kopfball", "Schusskraft", "Schussgenauigkeit", "Technik", "Spielintelligenz",
                "Reflexe", "Paraden", "Lufthoheit", "Herauslaufen", "Ballsicherheit");

        new AawParser().readFiles(path2, path3, path4, path5, path6).forEach(player -> {
            System.out.println("\n" + player.name + " (" + player.data.size() / player.data.keySet().size() + " AAWs)");
            ordering.sortedCopy(player.data.keySet()).forEach(key -> {
                List<Aaw> aaws = player.data.get(key);
                Range<Integer> range = new RangeCalculator().calculateRange(aaws);
                String paddedFrom = Strings.padStart(String.valueOf(range.lowerEndpoint()), 2, '0');
                String paddedTo = Strings.padStart(String.valueOf(range.upperEndpoint()), 2, '0');
                System.out.println(paddedFrom + "-" + paddedTo + "%  " + key);
            });
        });
    }

}
