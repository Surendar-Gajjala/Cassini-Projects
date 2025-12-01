package com.cassinisys.erp.model.production;

import javax.persistence.*;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;

@Entity
@Table(name = "ERP_PROCESSSTEP")
@PrimaryKeyJoinColumn(name = "STEP_ID")
@ApiObject(group = "PRODUCTION")
public class ERPProcessStep extends ERPObject {

   
	@ApiObjectField(required = true)
	private ERPProcess process;
	@ApiObjectField(required = true)
	private ERPWorkCenter workcenter;
	@ApiObjectField(required = true)
	private Integer sequenceNumber;
	@ApiObjectField
	private String description;
	@ApiObjectField(required = true)
	private String name;
	
	public ERPProcessStep(){
		super.setObjectType(ObjectType.PROCESSSTEP);
	}

	@ManyToOne(fetch =FetchType.EAGER)
	@JoinColumn(name = "PROCESS", nullable = false)
	public ERPProcess getProcess() {
		return process;
	}

	public void setProcess(ERPProcess process) {
		this.process = process;
	}



	@Column(name = "SEQUENCE_NUMBER", nullable = false)
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
	

	@ManyToOne(fetch =FetchType.EAGER)
	@JoinColumn(name = "WORKCENTER", nullable = false)
	public ERPWorkCenter getWorkcenter() {
		return workcenter;
	}

	public void setWorkcenter(ERPWorkCenter workcenter) {
		this.workcenter = workcenter;
	}
}
