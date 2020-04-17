package de.mmagic.anstoss.table;

import java.util.Comparator;
import java.util.Map;

class TableEntryComparator implements Comparator<Map.Entry<String, TableRow>> {

    @Override
    public int compare(Map.Entry<String, TableRow> t1, Map.Entry<String, TableRow> t2) {
        TableRow first = t1.getValue();
        TableRow second = t2.getValue();
        if (first.points.intValue() != second.points.intValue()) {
            return second.points.compareTo(first.points);
        }
        return Integer.compare(second.goals - second.goals2, first.goals - first.goals2);
    }
}
