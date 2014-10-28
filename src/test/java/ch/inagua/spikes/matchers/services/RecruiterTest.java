package ch.inagua.spikes.matchers.services;

import org.junit.Test;

import ch.inagua.spikes.matchers.models.Colleague;
import ch.inagua.spikes.matchers.services.Recruiter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static ch.inagua.spikes.matchers.matchers.IsColleagueBuilderMatcher.isColleagueWith;

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

		assertThat(colleague, isColleagueWith("Jacques", 0, null, null, "100000"));
	}

	@Test
	public void test_enroll_withBuilderMatcher() {
		final Colleague colleague = new Recruiter().enroll("Jacques");

		assertThat(colleague, isColleagueWith()._name("Jacques")._salary("100000")._service(null)._currentProject(null));
		assertThat(colleague, isColleagueWith()._name("Jacques")._salary("100000"));
		assertThat(colleague, isColleagueWith()._name("Jacques")); // Fail!
	}

}
