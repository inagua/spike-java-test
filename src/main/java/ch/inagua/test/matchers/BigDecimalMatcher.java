package ch.inagua.test.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.math.BigDecimal;

/**
 * Matcher to compare {@link BigDecimal} and dealing with decimals.
 *
 * Usage:
 * <pre>
 * assertThat(account.getBalance(), is(bd(123.45)));
 * </pre>
 *
 * See {@link BigDecimalMatcher} for more details.
 */
public class BigDecimalMatcher extends TypeSafeMatcher<BigDecimal> {

    final private BigDecimal bigDecimal;

    private BigDecimalMatcher(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    /**
     * Shortcut for {@link #bigDecimal(BigDecimal)}
     */
    @Factory
    public static Matcher<BigDecimal> bd(BigDecimal bd) {
        return bigDecimal(bd);
    }
    @Factory
    public static Matcher<BigDecimal> bigDecimal(BigDecimal bd) {
        return new BigDecimalMatcher(bd);
    }

    /**
     * Shortcut for {@link #bigDecimal(int)}
     */
    @Factory
    public static Matcher<BigDecimal> bd(int i) {
        return bigDecimal(i);
    }
    @Factory
    public static Matcher<BigDecimal> bigDecimal(int i) {
        return new BigDecimalMatcher(BigDecimal.valueOf(i));
    }

    /**
     * Shortcut for {@link #bigDecimal(long)}
     */
    @Factory
    public static Matcher<BigDecimal> bd(long l) {
        return bigDecimal(l);
    }
    @Factory
    public static Matcher<BigDecimal> bigDecimal(long l) {
        return new BigDecimalMatcher(BigDecimal.valueOf(l));
    }

    /**
     * Shortcut for {@link #bigDecimal(double)}
     */
    @Factory
    public static Matcher<BigDecimal> bd(double d) {
        return bigDecimal(d);
    }
    @Factory
    public static Matcher<BigDecimal> bigDecimal(double d) {
        return new BigDecimalMatcher(BigDecimal.valueOf(d));
    }

    /**
     * Shortcut for {@link #bigDecimal(String)}
     */
    @Factory
    public static Matcher<BigDecimal> bd(String s) {
        return bigDecimal(s);
    }
    @Factory
    public static Matcher<BigDecimal> bigDecimal(String s) {
        return new BigDecimalMatcher(new BigDecimal(s));
    }

    @Override
    public boolean matchesSafely(BigDecimal item) {
        // return bigDecimal.stripTrailingZeros().equals(item.stripTrailingZeros());
        return bigDecimal.compareTo(item) == 0;
    }

    @Override
    public void describeMismatchSafely(BigDecimal item, Description mismatchDescription) {
        mismatchDescription.appendText(" was a BigDecimal with value ").appendValue(item);
        if (bigDecimal != null && item != null) {
            mismatchDescription.appendText(" differed by:").appendValue(bigDecimal.subtract(item));
        }
    }

    public void describeTo(Description description) {
        description.appendText("a BigDecimal with value ").appendValue(bigDecimal);
    }

}
