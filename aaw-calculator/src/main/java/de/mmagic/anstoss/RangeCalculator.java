package de.mmagic.anstoss;

import com.google.common.collect.Range;

import java.util.List;

class RangeCalculator {

    Range<Integer> calculateRange(List<Aaw> aaws) {
        int from = 0;
        int to = 99;
        for (Aaw aaw : aaws) {
            if (aaw.upDown.equals("+1")) {
                from = Math.max(0, from + aaw.percent - 100);
                to = to + aaw.percent - 100;
                continue;
            }
            if (aaw.upDown.equals("-1")) {
                from = 100 + from + aaw.percent;
                to = Math.min(99, 100 + aaw.percent + to);
                continue;
            }
            if (aaw.percent >= 0) {
                from = from + aaw.percent;
                to = Math.min(99, to + aaw.percent);
            } else {
                from = 0;
                to = to + aaw.percent;
            }
        }
        return Range.closed(from, to);
    }
}
