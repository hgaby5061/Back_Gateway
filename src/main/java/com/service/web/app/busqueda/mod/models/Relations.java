package com.service.web.app.busqueda.mod.models;

public class Relations {
	
	private String governor;
	private String dependent;
	private String relation;	

	public Relations(String governor, String dependent, String relation) {
		this.governor=governor;
		this.dependent=dependent;
		this.relation=relation;
	}
	
	public String getGovernor() {
		return governor;
	}

	public void setGovernor(String governor) {
		this.governor = governor;
	}

	public String getDependent() {
		return dependent;
	}

	public void setDependent(String dependent) {
		this.dependent = dependent;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	

}
