package com.cassinisys.erp.model.production;

import com.cassinisys.erp.model.core.ERPObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by SubramanyamReddy on 02-02-2017.
 */
@Entity
@Table(name = "ERP_PROCESSSTEPINSTANCE")
@PrimaryKeyJoinColumn(name = "INSTANCE_ID")
@ApiObject(group = "PRODUCTION")
public class ERPProcessStepInstance extends ERPObject{

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@ApiObjectField(required = true)
	@Column(name = "PROCESS")
	private Integer process;

	@ApiObjectField(required = true)
	@Column(name = "WORKCENTER")
	private Integer workCenter;

	@ApiObjectField(required = true)
	@Column(name = "NAME")
	private String name;

	@ApiObjectField(required = true)
	@Column(name = "DESCRIPTION")
	private String description;

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getWorkCenter() {
		return workCenter;
	}

	public void setWorkCenter(Integer workCenter) {
		this.workCenter = workCenter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
