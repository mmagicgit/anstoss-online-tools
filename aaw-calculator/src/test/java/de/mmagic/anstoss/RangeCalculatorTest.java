package de.mmagic.anstoss;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class RangeCalculatorTest {

    private final RangeCalculator rangeCalculator = new RangeCalculator();

    @Test
    void addOneWithoutLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(17, ""));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(17, 99)));
    }

    @Test
    void addTwoWithoutLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(17, ""), new Aaw(15, ""));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(32, 99)));
    }

    @Test
    void subtractOneWithoutLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(-17, ""));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(0, 82)));
    }

    @Test
    void subtractTwoWithoutLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(-17, ""), new Aaw(-15, ""));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(0, 67)));
    }

    @Test
    void subtractAndAddWithoutLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(-17, ""), new Aaw(15, ""));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(15, 97)));
    }

    @Test
    void addAndSubtractWithoutLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(17, ""), new Aaw(-22, ""));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(0, 77)));
    }

}