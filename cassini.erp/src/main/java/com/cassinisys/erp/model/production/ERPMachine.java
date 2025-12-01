package com.cassinisys.erp.model.production;

import javax.persistence.*;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;

@Entity
@Table(name = "ERP_MACHINE")
@PrimaryKeyJoinColumn(name = "MACHINE_ID")
@ApiObject(group = "PRODUCTION")
public class ERPMachine extends ERPObject {
	
	
    @ApiObjectField
	private String description;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField(required = true)
	private ERPWorkCenter workcenter;
	
	public ERPMachine() {
		super.setObjectType(ObjectType.MACHINE);
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "WORKCENTER", nullable = false)
	public ERPWorkCenter getWorkcenter() {
		return workcenter;
	}

	public void setWorkcenter(ERPWorkCenter workcenter) {
		this.workcenter = workcenter;
	}
}
