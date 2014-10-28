package ch.inagua.spikes.matchers.models;

import java.math.BigDecimal;

public class Colleague {

	private String name;
	private String service;
	private int age;
	private String currentProject;
	private BigDecimal salary;

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getService() { return service; }
	public void setService(String service) { this.service = service; }

	public int getAge() { return age; }
	public void setAge(int age) { this.age = age; }

	public String getCurrentProject() { return currentProject; }
	public void setCurrentProject(String currentProject) { this.currentProject = currentProject; }

	public BigDecimal getSalary() { return salary; }
	public void setSalary(BigDecimal salary) { this.salary = salary; }

}
