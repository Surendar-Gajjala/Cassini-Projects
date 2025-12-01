package com.cassinisys.erp.model.production;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_PRODUCTINVENTORYHISTORY")
@ApiObject(group = "PRODUCTION")
public class ERPProductInventoryHistory {

	@ApiObjectField(required = true)
	private Integer product;
	@ApiObjectField(required = true)
	private InventoryType type = InventoryType.STOCKIN;
	@ApiObjectField(required = true)
	private Integer quantity = 0;
	@ApiObjectField(required = true)
	private Date timestamp;
	@ApiObjectField(required = true)
	private ERPEmployee employee;
	@ApiObjectField(required = true)
	private Integer rowId;
	@ApiObjectField(required = false)
	private Integer reference;

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

	@Column(name = "PRODUCT_ID")
	public Integer getProduct() {
		return product;
	}

	public void setProduct(Integer product) {
		this.product = product;
	}

	@Column(name = "IN_OR_OUT")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.InventoryType") })
	public InventoryType getType() {
		return type;
	}

	public void setType(InventoryType type) {
		this.type = type;
	}

	@Column(name = "QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE", nullable = false)
	public ERPEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(ERPEmployee employee) {
		this.employee = employee;
	}

	@Column(name = "REFERENCE")
	public Integer getReference() {
		return reference;
	}

	public void setReference(Integer reference) {
		this.reference = reference;
	}
}
