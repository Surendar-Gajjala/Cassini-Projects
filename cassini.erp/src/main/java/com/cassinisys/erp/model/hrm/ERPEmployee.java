package com.cassinisys.erp.model.hrm;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.converters.CustomShortDateDeserializer;
import com.cassinisys.erp.model.common.ERPPerson;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by reddy on 7/18/15.
 */
@Entity
@Table(name = "ERP_EMPLOYEE")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "EMPLOYEE_ID")
@ApiObject(group = "HRM")
public class ERPEmployee extends ERPPerson {

	@ApiObjectField
	private String employeeNumber;
	@ApiObjectField
	private ERPBusinessUnit businessUnit;
	@ApiObjectField(required = true)
	private ERPEmployeeType employeeType;
	@ApiObjectField(required = true)
	private ERPDepartment department;
	@ApiObjectField
	private String jobTitle;
	@ApiObjectField
	private Date dateOfBirth;
	@ApiObjectField
	private Date dateOfHire;
	@ApiObjectField(required = true)
	private EmployeeStatus status = EmployeeStatus.ACTIVE;
	@ApiObjectField
	private Integer manager;
	@ApiObjectField
	private byte[] picture;

	private String managerName;

	public ERPEmployee() {
		super.setObjectType(ObjectType.EMPLOYEE);
	}

	@Column(name = "EMPLOYEE_NUMBER")
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BUSINESS_UNIT")
	public ERPBusinessUnit getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(ERPBusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE_TYPE")
	public ERPEmployeeType getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(ERPEmployeeType employeeType) {
		this.employeeType = employeeType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DEPARTMENT")
	public ERPDepartment getDepartment() {
		return department;
	}

	public void setDepartment(ERPDepartment department) {
		this.department = department;
	}

	@Column(name = "JOB_TITLE")
	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@Column(name = "DATE_OF_BRITH")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomShortDateDeserializer.class)
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Column(name = "DATE_OF_HIRE")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomShortDateDeserializer.class)
	public Date getDateOfHire() {
		return dateOfHire;
	}

	public void setDateOfHire(Date dateOfHire) {
		this.dateOfHire = dateOfHire;
	}

	@Column(name = "STATUS")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.hrm.EmployeeStatus") })
	public EmployeeStatus getStatus() {
		return status;
	}

	public void setStatus(EmployeeStatus status) {
		this.status = status;
	}

	@Column(name = "MANAGER")
	public Integer getManager() {
		return manager;
	}

	public void setManager(Integer manager) {
		this.manager = manager;
	}

	@Column(name = "PICTURE")
	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	@Transient
	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
}
