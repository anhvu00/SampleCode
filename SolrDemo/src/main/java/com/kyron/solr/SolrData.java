package com.kyron.solr;

import org.apache.solr.client.solrj.beans.Field;

public class SolrData {
	@Field public String id;
	@Field public String name;
	@Field public Long age;
	@Field public String occupation;
	
	// constructors
	
	public SolrData() {
	}
	
	public SolrData(String id, String name, Long age, String occ) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.occupation = occ;
	}
	
	@Override
	public String toString() {
		return ("id=" + id + ",name=" + name + ",age=" + age + ",occupation=" + occupation);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

}
