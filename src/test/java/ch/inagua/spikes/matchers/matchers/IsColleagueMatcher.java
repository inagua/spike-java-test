package ch.inagua.spikes.matchers.matchers;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import ch.inagua.spikes.matchers.models.Colleague;

public class IsColleagueMatcher extends TypeSafeMatcher<Colleague> {

	private final String name;
	private final int age;
	private final String service;
	private final String currentProject;
	private final BigDecimal salary;

	/**
	 * Constructor, private!... @see {@link #isColleagueWith(String, int, String, String, String)}
	 */
	private IsColleagueMatcher(String name, int age, String service, String currentProject, BigDecimal salary) {
		this.name = name;
		this.age = age;
		this.service = service;
		this.currentProject = currentProject;
		this.salary = salary;
	}

	/**
	 * Static method to return an instance of the matcher
	 */
	@Factory
	public static IsColleagueMatcher isColleagueWith(String name, int age, String service, String currentProject, String salary) {
		return new IsColleagueMatcher(name, age, service, currentProject, new BigDecimal(salary));
	}

	/**
	 * toString method for the Expected (values given to the factory above)
	 */
	public void describeTo(Description description) {
		description.appendText("colleague with properties [" //
				+ "name=" + name //
				+ ", age=" + age //
				+ ", service=" + service //
				+ ", currentProject=" + currentProject //
				+ ", salary=" + salary //
				+ "]");
	}

	/**
	 * toString method for the Actual / tested instance of the object
	 */
	@Override
	protected void describeMismatchSafely(Colleague colleague, Description description) {
		description.appendText("was [" //
				+ (StringUtils.equals(name, colleague.getName()) ? "" : "name=" + colleague.getName()) //
				+ (age == colleague.getAge() ? "" : ", age=" + colleague.getAge()) //
				+ (StringUtils.equals(service, colleague.getService()) ? "" : ", service=" + colleague.getService()) //
				+ (StringUtils.equals(currentProject, colleague.getCurrentProject()) ? "" : ", currentProject=" + colleague.getCurrentProject()) //
				+ (areBigDecimalEquals(salary, colleague.getSalary()) ? "" : ", salary=" + colleague.getSalary()) //
				+ "]");
	}

	/**
	 * Do the comparison!
	 */
	@Override
	protected boolean matchesSafely(Colleague colleague) {
		return true //
				&& StringUtils.equals(name, colleague.getName())//
				&& age == colleague.getAge()//
				&& StringUtils.equals(service, colleague.getService())//
				&& StringUtils.equals(currentProject, colleague.getCurrentProject())//
				&& areBigDecimalEquals(salary, colleague.getSalary())//
		;
	}

	/**
	 * Private stuff
	 */
	private boolean areBigDecimalEquals(BigDecimal bd1, BigDecimal bd2) {
		if (bd1 == null && bd2 == null)
			return true;
		if (bd1 != null)
			return bd1.equals(bd2);
		return bd2.equals(bd1);
	}

}
