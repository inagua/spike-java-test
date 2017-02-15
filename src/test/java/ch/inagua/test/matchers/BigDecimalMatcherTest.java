package ch.inagua.test.matchers;

import org.junit.Test;

import java.math.BigDecimal;

import static ch.inagua.test.matchers.BigDecimalMatcher.bd;
import static ch.inagua.test.matchers.BigDecimalMatcher.bigDecimal;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public class BigDecimalMatcherTest {

    private static final BigDecimal BIG_DECIMAL_42 = BigDecimal.valueOf(42.00);

    private static final BigDecimal BIG_DECIMAL_123_45 = new BigDecimal("123.450");

    @Test
    public void test_withInt() {
        assertThat(BigDecimal.ZERO, is(bd(0)));
        assertThat(BigDecimal.ZERO, is(bigDecimal(0)));

        assertThat(BIG_DECIMAL_42, is(bd(42)));
        assertThat(BIG_DECIMAL_123_45, is(not(bd(123))));
        assertThat(BIG_DECIMAL_123_45, is(not(bd(33))));

        assertThat(BIG_DECIMAL_42, is(bigDecimal(42)));
        assertThat(BIG_DECIMAL_123_45, is(not(bigDecimal(123))));
        assertThat(BIG_DECIMAL_123_45, is(not(bigDecimal(33))));
    }

    @Test
    public void test_withLong() {
        assertThat(BigDecimal.ZERO, is(bd(0L)));
        assertThat(BigDecimal.ZERO, is(bigDecimal(0L)));

        assertThat(BIG_DECIMAL_42, is(bd(42L)));
        assertThat(BIG_DECIMAL_123_45, is(not(bd(123L))));
        assertThat(BIG_DECIMAL_123_45, is(not(bd(33L))));

        assertThat(BIG_DECIMAL_42, is(bigDecimal(42L)));
        assertThat(BIG_DECIMAL_123_45, is(not(bigDecimal(123L))));
        assertThat(BIG_DECIMAL_123_45, is(not(bigDecimal(33L))));
    }

    @Test
    public void test_withDouble() {
        assertThat(BigDecimal.ZERO, is(bd(0.)));
        assertThat(BigDecimal.ZERO, is(bigDecimal(0.)));

        assertThat(BIG_DECIMAL_42, is(bd(42.)));
        assertThat(BIG_DECIMAL_123_45, is(bd(123.450)));
        assertThat(BIG_DECIMAL_123_45, is(not(bd(33.33))));

        assertThat(BIG_DECIMAL_42, is(bigDecimal(42.)));
        assertThat(BIG_DECIMAL_123_45, is(bigDecimal(123.450)));
        assertThat(BIG_DECIMAL_123_45, is(not(bigDecimal(33.33))));
    }

    @Test
    public void test_withString() {
        assertThat(BigDecimal.ZERO, is(bd("0")));
        assertThat(BigDecimal.ZERO, is(bigDecimal("0")));
        assertThat(BigDecimal.ZERO, is(bd("0.0")));
        assertThat(BigDecimal.ZERO, is(bigDecimal("0.0")));

        assertThat(BIG_DECIMAL_42, is(bd("42.00")));
        assertThat(BIG_DECIMAL_123_45, is(bd("123.450")));
        assertThat(BIG_DECIMAL_123_45, is(not(bd("33.33"))));

        assertThat(BIG_DECIMAL_42, is(bigDecimal("42.00")));
        assertThat(BIG_DECIMAL_123_45, is(bigDecimal("123.450")));
        assertThat(BIG_DECIMAL_123_45, is(not(bigDecimal("33.33"))));
    }

    @Test
    public void test_withBigDecimal() {
        assertThat(BigDecimal.ZERO, is(bd(new BigDecimal(0.0))));
        assertThat(BigDecimal.ZERO, is(bigDecimal(new BigDecimal("0."))));

        assertThat(BIG_DECIMAL_42, is(bd(new BigDecimal("42"))));
        assertThat(BIG_DECIMAL_123_45, is(bd(BigDecimal.valueOf(123.450))));
        assertThat(BIG_DECIMAL_123_45, is(not(bd(BigDecimal.valueOf(33.33)))));

        assertThat(BIG_DECIMAL_42, is(bigDecimal(new BigDecimal("42"))));
        assertThat(BIG_DECIMAL_123_45, is(bigDecimal(BigDecimal.valueOf(123.450))));
        assertThat(BIG_DECIMAL_123_45, is(not(bigDecimal(BigDecimal.valueOf(33.33)))));
    }

}
