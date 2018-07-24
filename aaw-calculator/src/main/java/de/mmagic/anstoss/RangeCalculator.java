package de.mmagic.anstoss;

import com.google.common.collect.Range;

import java.util.List;

class RangeCalculator {

    Range<Integer> calculateRange(List<Aaw> aaws) {
        int from = 0;
        int to = 99;
        for (Aaw aaw : aaws) {
            if (aaw.upDown.equals("+1")) {
                from = 0;
                to = aaw.percent - 1;
                continue;
            }
            if (aaw.upDown.equals("-1")) {
                from = 100 + aaw.percent;
                to = 99;
                continue;
            }
            if (aaw.percent >= 0) {
                from = from + aaw.percent;
                to = Math.min(to + aaw.percent, 99);
            } else {
                from = 0;
                to = to + aaw.percent;
            }
        }
        return Range.closed(from, to);
    }
}
