package de.mmagic.anstoss;

import com.google.common.base.Strings;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;

import java.io.IOException;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        String path = "/home/ms/Entwicklung/Git/anstoss-online-tools/aaw-calculator/src/main/resources/AAW_Sommer_2017.json";
        String path2 = "/home/ms/Entwicklung/Git/anstoss-online-tools/aaw-calculator/src/main/resources/AAW_Winter_2017.html";
        String path3 = "/home/ms/Entwicklung/Git/anstoss-online-tools/aaw-calculator/src/main/resources/AAW_Sommer_2018.html";

        Ordering<String> ordering = Ordering.explicit("Schnelligkeit", "Zweikampf", "Kopfball", "Schusskraft", "Schussgenauigkeit", "Technik", "Spielintelligenz");

        new AawParser().readFiles(path, path2, path3).forEach(player -> {
            System.out.println("\n" + player.name);
            ordering.sortedCopy(player.data.keySet()).forEach(key -> {
                List<Aaw> aaws = player.data.get(key);
                Range<Integer> range = new RangeCalculator().calculateRange(aaws);
                String paddedProperty = Strings.padEnd(key + ": ", 19, ' ');
                String paddedFrom = Strings.padStart(String.valueOf(range.lowerEndpoint()), 2, ' ');
                String paddedTo = Strings.padStart(String.valueOf(range.upperEndpoint()), 2, ' ');
                System.out.println(paddedProperty + paddedFrom + "-" + paddedTo + "%");
            });
            System.out.println("Betrachtete AAWs: " + player.data.size() / 7);
        });
    }

}
