package com.cassinisys.erp.model.hrm;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomShortDateDeserializer;
import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ERP_EMPLOYEETIMEOFF")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "HRM")
public class ERPEmployeeTimeOff implements Serializable {

	@ApiObjectField(required = true)
	private Integer rowId;
	@ApiObjectField(required = true)
	private Integer employeeId;
	@ApiObjectField(required = true)
	private Integer timeOffType;
	@ApiObjectField(required = true)
	private Date startDate;
	@ApiObjectField(required = true)
	private Date endDate;
	@ApiObjectField(required = true)
	private TimeOffStatus status=TimeOffStatus.PENDING;
	@ApiObjectField
	private String reason;
	@ApiObjectField(required = true)
	private Integer numOfDays;
	
	
	private String empName;
	
	private String leaveType;	

	@Id
	@SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
	@Column(name = "ROWID")
	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	@Column(name = "EMPLOYEE_ID", nullable = false)
	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	@Column(name = "TIMEOFF_TYPE", nullable = false)
	public Integer getTimeOffType() {
		return timeOffType;
	}

	public void setTimeOffType(Integer timeOffType) {
		this.timeOffType = timeOffType;
	}
			
	@Column(name = "STATUS")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.hrm.TimeOffStatus") })
	public TimeOffStatus getStatus() {
		return status;
	}

	@Column(name = "START_DATE", nullable = false)
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomShortDateDeserializer.class)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "END_DATE", nullable = false)
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomShortDateDeserializer.class)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStatus(TimeOffStatus status) {
		this.status = status;
	}

	@Column(name = "REASON")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Column(name = "NUM_OF_DAYS")
	public Integer getNumOfDays() {
		return numOfDays;
	}

	public void setNumOfDays(Integer numOfDays) {
		this.numOfDays = numOfDays;
	}
	
	@Transient
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	@Transient
	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
}
