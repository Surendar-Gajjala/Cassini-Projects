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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cassinisys.erp.converters.CustomShortDateDeserializer;
import com.cassinisys.erp.converters.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ERP_EMPLOYEEATTENDANCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "HRM")
public class ERPEmployeeAttendance implements Serializable {

	@ApiObjectField(required = true)
	private String empNumber;
	@ApiObjectField(required = true)
	private Date inTime;
	@ApiObjectField
	private Date outTime;
	@ApiObjectField
	private Date date;
	@ApiObjectField(required = true)
	private Integer rowId;
	@ApiObjectField
	private AttendanceStatus status=AttendanceStatus.A;

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

	@Column(name = "EMPLOYEE_NUMBER", nullable = false)
	public String getEmpNumber() {
		return empNumber;
	}

	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}

	@Column(name = "IN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	@Column(name = "OUT_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	@Column(name = "DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonDeserialize(using= CustomShortDateDeserializer.class)
	@JsonSerialize(using = CustomShortDateSerializer.class)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "STATUS")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.hrm.AttendanceStatus") })
	public AttendanceStatus getStatus() {
		return status;
	}

	public void setStatus(AttendanceStatus status) {
		this.status = status;
	}
}
