package ch.inagua.spikes.matchers.matchers;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import ch.inagua.spikes.matchers.models.Colleague;

public class IsColleagueBuilderMatcher extends TypeSafeMatcher<Colleague> {

	//
	// MACTHER PART
	//

	/**
	 * Constructor, private!... @see {@link #isColleagueWith()}
	 */
	private IsColleagueBuilderMatcher() {
	}

	/**
	 * Static method to return an instance of the matcher
	 */
	@Factory
	public static IsColleagueBuilderMatcher isColleagueWith() {
		return new IsColleagueBuilderMatcher();
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

	//
	// BUILDER part
	//

	private String name;
	private int age;
	private String service;
	private String currentProject;
	private BigDecimal salary;

	/**
	 * Setter for name
	 */
	public IsColleagueBuilderMatcher _name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Setter for age
	 */
	public IsColleagueBuilderMatcher _age(int age) {
		this.age = age;
		return this;
	}

	/**
	 * Setter for service
	 */
	public IsColleagueBuilderMatcher _service(String service) {
		this.service = service;
		return this;
	}

	/**
	 * Setter for currentProject
	 */
	public IsColleagueBuilderMatcher _currentProject(String currentProject) {
		this.currentProject = currentProject;
		return this;
	}

	/**
	 * Setter for salary
	 */
	public IsColleagueBuilderMatcher _salary(String salary) {
		this.salary = new BigDecimal(salary);
		return this;
	}

}
