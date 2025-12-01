package com.cassinisys.erp.model.production;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;

@Entity
@Table(name = "ERP_MACHINESPAREPART")
@PrimaryKeyJoinColumn(name = "PART_ID")
@ApiObject(group = "PRODUCTION")
public class ERPMachineSparePart extends ERPObject {
		
	@ApiObjectField
	private String description;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField(required = true)
	private Integer machine;
	
	public ERPMachineSparePart() {
		super.setObjectType(ObjectType.MACHINESPAREPART);
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

	@Column(name = "MACHINE", nullable = false)
	public Integer getMachine() {
		return machine;
	}

	public void setMachine(Integer machine) {
		this.machine = machine;
	}
	
	
}
