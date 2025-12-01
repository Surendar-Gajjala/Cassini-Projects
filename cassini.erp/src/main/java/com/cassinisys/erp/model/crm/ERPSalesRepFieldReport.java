package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ERP_SALESREPFIELDREPORT")
@PrimaryKeyJoinColumn(name = "REPORT_ID")
@ApiObject(group = "CRM")
public class ERPSalesRepFieldReport extends ERPObject {

	@ApiObjectField(required = true)
	private ERPSalesRep salesRep;
	@ApiObjectField(required = true)
	private ERPCustomer customer;
	@ApiObjectField(required = true)
	private Date timestamp;
	@ApiObjectField(required = true)
	private String notes;


	public ERPSalesRepFieldReport() {
		super.setObjectType(ObjectType.SALESREPFIELDREPORT);
	}

	@ManyToOne
	@JoinColumn(name = "SALESREP", nullable = false)
	public ERPSalesRep getSalesRep() {
		return salesRep;
	}

	public void setSalesRep(ERPSalesRep salesRep) {
		this.salesRep = salesRep;
	}

	@ManyToOne
	@JoinColumn(name = "CUSTOMER", nullable = false)
	public ERPCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(ERPCustomer customer) {
		this.customer = customer;
	}

	@Column(name = "TIMESTAMP", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
