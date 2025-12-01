package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.model.production.ERPProduct;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Entity
@Table(name = "ERP_CUSTOMERRETURNDETAILS")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CRM")
public class ERPCustomerReturnDetails {

	@ApiObjectField(required = true)
	private Integer rowId;
	@ApiObjectField(required = true)
	private ERPCustomerReturn customerReturn;
	@ApiObjectField(required = true)
	private ERPProduct product;
	@ApiObjectField(required = true)
	private Integer quantity;

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

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID", nullable = false)
	public ERPCustomerReturn getCustomerReturn() {
		return this.customerReturn;
	}

	public void setCustomerReturn(ERPCustomerReturn customerReturn) {
		this.customerReturn = customerReturn;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	public ERPProduct getProduct() {
		return product;
	}

	public void setProduct(ERPProduct product) {
		this.product = product;
	}

	@Column(name = "QUANTITY", nullable = false)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
