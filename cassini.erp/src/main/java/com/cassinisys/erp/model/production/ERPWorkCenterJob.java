package com.cassinisys.erp.model.production;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ERP_WORKCENTERJOB")
@PrimaryKeyJoinColumn(name = "JOB_ID")
@ApiObject(group = "PRODUCTION")
public class ERPWorkCenterJob extends ERPObject {
	
	//PROCESS_STEP
    @ApiObjectField
	private Date scheduledDate;
    @ApiObjectField
	private Date finishDate;
	@ApiObjectField
	private Date startDate;
	@ApiObjectField(required = true)
	private String status;    
	@ApiObjectField(required = true)
	private String processStep;
	
	 public ERPWorkCenterJob() {
		super.setObjectType(ObjectType.WORKCENTERJOB);
	}
    
	
	@Column(name = "PROCESS_STEP", nullable = false)
	public String getProcessStep() {
		return processStep;
	}
	
	public void setProcessStep(String processStep) {
		this.processStep = processStep;
	}

	@Column(name = "SCHEDULED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	@Column(name = "FINISH_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	@Column(name = "START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	
	@Column(name = "STATUS", nullable = false)
	@Type(type = "com.cassinisys.erp.converters.EnumUserType",
			parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
					value = "com.cassinisys.erp.model.production.JobStatus") })
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
   
	
}
