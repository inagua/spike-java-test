package ch.inagua.test.matchers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ch.inagua.test.matchers.CollectionMatcher.single;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by jacques@inagua.ch on 09.02.2017.
 */
public class CollectionMatcherTest {

    @Test
    public void test_single_shouldReturnTheOnlyOneElement() throws Exception {
        final List<Long> ids = asList(42L);
        assertThat(single(ids), equalTo(42L));

        assertThat(single(asList("42")), equalTo("42"));
    }

    @Test(expected = AssertionError.class)
    public void test_single_shouldFailWithSeveralElements() throws Exception {
        single(asList(11L, 22L, 33L));
    }

    @Test(expected = AssertionError.class)
    public void test_single_shouldFailWithNullCollection() throws Exception {
        final List<Long> ids = null;
        CollectionMatcher.single(ids);
    }

    @Test(expected = AssertionError.class)
    public void test_single_shouldFailWithEmptyCollection() throws Exception {
        final List<Long> ids = new ArrayList<Long>();
        CollectionMatcher.single(ids);
    }

    @Test
    public void test_hasExactlyItems_shouldSucceedWithTheRightItems() {
        assertThat(Arrays.asList("a", "b", "c"), CollectionMatcher.hasExactlyItems("b", "c", "a"));
        assertThat(Arrays.asList(11L, 22L, 33L), CollectionMatcher.hasExactlyItems(11L, 22L, 33L));
    }

    @Test(expected = AssertionError.class)
    public void test_hasExactlyItems_shouldFailedWithMissingItems() {
        assertThat(Arrays.asList("a", "b", "c"), CollectionMatcher.hasExactlyItems("a", "b"));
    }
}
