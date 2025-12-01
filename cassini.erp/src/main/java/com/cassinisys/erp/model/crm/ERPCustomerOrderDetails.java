package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.model.production.ERPProduct;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ERP_CUSTOMERORDERDETAILS")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CRM")
public class ERPCustomerOrderDetails implements Serializable {

	@ApiObjectField(required = true)
	private Integer rowId;
	@ApiObjectField(required = true)
	private Integer serialNumber;
	@ApiObjectField
	private Integer orderId;
	@ApiObjectField(required = true)
	private ERPProduct product;
	@ApiObjectField(required = true)
	private Integer quantity;
	@ApiObjectField
	private Integer quantityShipped;
	@ApiObjectField
	private Integer boxes;
	@ApiObjectField(required = true)
	private Double unitPrice;
	@ApiObjectField
	private Double discount;
	@ApiObjectField
	private Double itemTotal;

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

	@Column(name = "SERIAL_NUMBER")
	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	@Column(name = "ORDER_ID")
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	@Column(name = "QUANTITY_SHIPPED", nullable = false)
	public Integer getQuantityShipped() {
		return quantityShipped;
	}

	public void setQuantityShipped(Integer quantityShipped) {
		this.quantityShipped = quantityShipped;
	}

	@Column(name = "BOXES", nullable = true)
	public Integer getBoxes() {
		return boxes;
	}

	public void setBoxes(Integer boxes) {
		this.boxes = boxes;
	}

	@Column(name = "UNIT_PRICE", nullable = false)
	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "DISCOUNT")
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@Column(name = "ITEM_TOTAL", nullable = false)
	public Double getItemTotal() {
		return itemTotal;
	}

	public void setItemTotal(Double itemTotal) {
		this.itemTotal = itemTotal;
	}

}
