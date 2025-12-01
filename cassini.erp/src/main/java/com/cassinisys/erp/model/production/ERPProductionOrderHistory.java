package com.cassinisys.erp.model.production;

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

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_PRODUCTIONORDERHISTORY")
@ApiObject(group = "PRODUCTION")
public class ERPProductionOrderHistory {

	@ApiObjectField(required = true)
	private Integer productionOrder;
	@ApiObjectField
	private ProductionOrderStatus oldStatus;
	@ApiObjectField(required = true)
	private ProductionOrderStatus newStatus;
	@ApiObjectField(required = true)
	private Date timestamp;
	@ApiObjectField(required = true)
	private String notes;
	@ApiObjectField(required = true)
	private Integer rowId;

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

	@Column(name = "ORDER_ID")
	public Integer getProductionOrder() {
		return productionOrder;
	}

	public void setProductionOrder(Integer productionOrder) {
		this.productionOrder = productionOrder;
	}

	@Column(name = "OLD_STATUS")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.ProductionOrderStatus") })
	public ProductionOrderStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(ProductionOrderStatus oldStatus) {
		this.oldStatus = oldStatus;
	}

	@Column(name = "NEW_STATUS")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.ProductionOrderStatus") })
	public ProductionOrderStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(ProductionOrderStatus newStatus) {
		this.newStatus = newStatus;
	}

	@Column(name = "TIMESTAMP")
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
