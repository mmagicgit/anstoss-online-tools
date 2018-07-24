package de.mmagic.anstoss;

import com.google.common.collect.Range;

import java.io.IOException;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        String path = "/home/ms/Entwicklung/Git/anstoss-online-tools/aaw-calculator/src/main/resources/AAW_Winter_2017.html";
        String path2 = "/home/ms/Entwicklung/Git/anstoss-online-tools/aaw-calculator/src/main/resources/AAW_Sommer_2018.html";

        new AawParser().readFiles(path, path2).forEach(player -> {
            System.out.println("\n" + player.name);
            player.data.keySet().forEach(key -> {
                List<Aaw> aaws = player.data.get(key);
                Range<Integer> range = new RangeCalculator().calculateRange(aaws);
                System.out.println(key + ": " + range.lowerEndpoint() + "-" + range.upperEndpoint() + "%");
            });
        });
    }

}
