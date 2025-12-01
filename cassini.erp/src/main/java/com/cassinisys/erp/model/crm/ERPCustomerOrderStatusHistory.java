package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.converters.CustomDateDeserializer;
import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ERP_CUSTOMERORDERSTATUSHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CRM")
public class ERPCustomerOrderStatusHistory {

	@ApiObjectField(required = true)
	private Integer rowId;
	@ApiObjectField(required = true)
	private ERPCustomerOrder order;
	@ApiObjectField(required = true)
	private Date modifiedDate;
	@ApiObjectField(required = true)
	private ERPEmployee modifiedBy;
	@ApiObjectField
	private CustomerOrderStatus oldStatus;
	@ApiObjectField(required = true)
	private CustomerOrderStatus newStatus;

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

	@JsonBackReference (value = "orderHistory")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORDER_ID", nullable = false, updatable = true, insertable = true)
	public ERPCustomerOrder getOrder() {
		return order;
	}

	public void setOrder(ERPCustomerOrder order) {
		this.order = order;
	}

	@Column(name = "MODIFIED_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MODIFIED_BY", nullable = false)
	public ERPEmployee getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(ERPEmployee modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "OLD_STATUS", nullable = false)
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.crm.CustomerOrderStatus") })
	public CustomerOrderStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(CustomerOrderStatus oldStatus) {
		this.oldStatus = oldStatus;
	}

	@Column(name = "NEW_STATUS", nullable = false)
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.crm.CustomerOrderStatus") })
	public CustomerOrderStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(CustomerOrderStatus newStatus) {
		this.newStatus = newStatus;
	}

}
