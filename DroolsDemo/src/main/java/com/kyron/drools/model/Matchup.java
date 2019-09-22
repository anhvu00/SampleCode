package com.kyron.drools.model;

import com.kyron.drools.model.Team;

public class Matchup {
	private Team home;
	private Team visitor;
	public Matchup(Team home, Team visitor) {
		super();
		this.home = home;
		this.visitor = visitor;
	}
	public Team getHome() {
		return home;
	}
	public void setHome(Team home) {
		this.home = home;
	}
	public Team getVisitor() {
		return visitor;
	}
	public void setVisitor(Team visitor) {
		this.visitor = visitor;
	}
	
	
}
