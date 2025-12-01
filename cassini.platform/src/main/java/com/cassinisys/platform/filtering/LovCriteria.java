package com.cassinisys.platform.filtering;


/**
 * @author reddy
 */
public class LovCriteria extends Criteria {

	private String type;
	private String name;

	public LovCriteria() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
