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
    void subtractTwoWithoutLevelChange2() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(61, ""), new Aaw(-5, ""));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(56, 94)));
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

    @Test
    void addTwoWithLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(75, ""), new Aaw(30, "+1"));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(5, 29)));
    }

    @Test
    void addThreeWithLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(11, "+1"), new Aaw(57, ""), new Aaw(42, "+1"));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(0, 9)));
    }

    @Test
    void subtractOneWithLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(-11, "-1"));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(89, 99)));
    }

    @Test
    void subtractTwoWithLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(11, ""), new Aaw(-21, "-1"));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(90, 99)));
    }

    @Test
    void subtractThreeWithLevelChange() {
        List<Aaw> aaws = Lists.newArrayList(new Aaw(40, "+1"), new Aaw(20, ""), new Aaw(-80, "-1"));
        assertThat(rangeCalculator.calculateRange(aaws), is(Range.closed(40, 79)));
    }

}