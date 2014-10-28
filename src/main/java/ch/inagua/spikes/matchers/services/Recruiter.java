package ch.inagua.spikes.matchers.services;

import java.math.BigDecimal;

import ch.inagua.spikes.matchers.models.Colleague;

public class Recruiter {

	public Colleague enroll(String name) {
		return ColleagueBuilder.colleagueWith().name(name).salary(new BigDecimal("100000")).build();
	}

}
