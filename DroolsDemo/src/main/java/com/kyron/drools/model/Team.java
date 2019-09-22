package com.kyron.drools.model;

/**
 * The idea is to load each Team object with everything needed for the rules (comparison/fire)
 * Create simple rules then chain them
 * Example:
 * - Home team usually wins
 * - Visitor team of same division usually wins
 * @author anh
 *
 */
public class Team {
	private boolean homeTeam;
	private String division;
	private String name;
	
	public Team(String name, boolean isHomeTeam, String division) {
		super();
		this.name = name;
		this.homeTeam = isHomeTeam;
		this.division = division;
	}
	
	public void setHomeTeam(boolean isHomeTeam) {
		this.homeTeam = isHomeTeam;
	}
	public boolean getHomeTeam() {
		return this.homeTeam;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
