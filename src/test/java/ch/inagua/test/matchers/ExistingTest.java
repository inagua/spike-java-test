package ch.inagua.test.matchers;

import org.junit.Test;

import java.math.BigDecimal;

import static ch.inagua.test.matchers.Existing.existing;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by jacques@inagua.ch on 09.02.2017.
 */
public class ExistingTest {

    @Test
    public void test_existing_shouldReturnTheNoNullParameter() throws Exception {
        final BigDecimal theAnswer = BigDecimal.valueOf(42.);
        assertThat(existing(theAnswer), equalTo(theAnswer));
        assertThat(existing(theAnswer).intValue(), equalTo(42));
    }

    @Test(expected = AssertionError.class)
    public void test_existing_shouldFailWithNullParamater() throws Exception {
        final BigDecimal theAnswer = null;
        existing(theAnswer);
    }

}
