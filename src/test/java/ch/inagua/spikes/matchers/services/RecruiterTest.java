package ch.inagua.spikes.matchers.services;

import ch.inagua.spikes.matchers.matchers.IsColleagueMatcher;
import ch.inagua.spikes.matchers.models.Colleague;
import org.junit.Test;

import static ch.inagua.spikes.matchers.matchers.IsColleagueBuilderMatcher.IgnoringNullProperties;
import static ch.inagua.spikes.matchers.matchers.IsColleagueBuilderMatcher.isColleagueWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RecruiterTest {

	@Test
	public void test_enroll_withAssertEquals() {
		final Colleague colleague = new Recruiter().enroll("Jacques");

		assertEquals(colleague.getName(), "Jacques");
		assertNull(colleague.getService());
		assertEquals(colleague.getAge(), 0);
		assertNull(colleague.getCurrentProject());
		assertEquals(colleague.getSalary().longValue(), 100000L);
	}

	@Test
	public void test_enroll_withMatcher() {
		final Colleague colleague = new Recruiter().enroll("Jacques");

		assertThat(colleague, IsColleagueMatcher.isColleagueWith("Jacques", 0, null, null, "100000"));
	}

	@Test
	public void test_enroll_withBuilderMatcher() {
		final Colleague colleague = new Recruiter().enroll("Jacques");

		assertThat(colleague, isColleagueWith(IgnoringNullProperties)._name("Jacques")._salary("100000")._service(null)._currentProject(null));
		assertThat(colleague, isColleagueWith(IgnoringNullProperties)._name("Jacques")._salary("100000"));
		assertThat(colleague, isColleagueWith(!IgnoringNullProperties)._name("Jacques")._salary("100000"));
	}

	@Test(expected = AssertionError.class)
	public void test_enroll_withBuilderMatcherShouldFailIfDoNotIgnoreNullProperties() {
		final Colleague colleague = new Recruiter().enroll("Jacques");

		assertThat(colleague, isColleagueWith(!IgnoringNullProperties)._name("Jacques")); // Fail!
	}

}
