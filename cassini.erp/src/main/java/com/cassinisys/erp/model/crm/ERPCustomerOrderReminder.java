package com.cassinisys.erp.model.crm;

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

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ERP_CUSTOMERODERREMINDER")
@ApiObject(group = "CRM")
public class ERPCustomerOrderReminder {

	@ApiObjectField(required = true)
	private Integer reminderId;
	@ApiObjectField(required = true)
	private Integer customer;
	@ApiObjectField(required = true)
	private String note;
	@ApiObjectField(required = true)
	private Date timestamp;
	@ApiObjectField(required = true)
	private Boolean disabled;

	@Id
	@SequenceGenerator(name = "CUSTOMERODERREMINDER_ID_GEN", sequenceName = "CUSTOMERODERREMINDER_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMERODERREMINDER_ID_GEN")
	@Column(name = "REMINDER_ID")
	public Integer getReminderId() {
		return reminderId;
	}

	public void setReminderId(Integer reminderId) {
		this.reminderId = reminderId;
	}

	@Column(name = "CUSTOMER_ID", nullable = false)
	public Integer getCustomer() {
		return customer;
	}

	public void setCustomer(Integer customer) {
		this.customer = customer;
	}

	@Column(name = "NOTE", nullable = false)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	@Column(name = "DISABLED", nullable = false)
	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

}
