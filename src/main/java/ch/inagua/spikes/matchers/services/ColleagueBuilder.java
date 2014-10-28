package ch.inagua.spikes.matchers.services;

import java.math.BigDecimal;

import ch.inagua.spikes.matchers.models.Colleague;

public class ColleagueBuilder {

	private ColleagueBuilder() {
	}

	public static ColleagueBuilder colleagueWith() {
		return new ColleagueBuilder();
	}

	public Colleague build() {
		final Colleague colleague = new Colleague();
		colleague.setName(name);
		colleague.setAge(age);
		colleague.setService(service);
		colleague.setCurrentProject(currentProject);
		colleague.setSalary(salary);
		return colleague;
	}

	private String name;
	private int age;
	private String service;
	private String currentProject;
	private BigDecimal salary;

	public ColleagueBuilder name(String name) {
		this.name = name;
		return this;
	}

	public ColleagueBuilder service(String service) {
		this.service = service;
		return this;
	}

	public ColleagueBuilder age(int age) {
		this.age = age;
		return this;
	}

	public ColleagueBuilder currentProject(String currentProject) {
		this.currentProject = currentProject;
		return this;
	}

	public ColleagueBuilder salary(BigDecimal salary) {
		this.salary = salary;
		return this;
	}

}
