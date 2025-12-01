package com.cassinisys.erp.model.production;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_PRODUCTIONORDERITEM")
@ApiObject(group = "PRODUCTION")
public class ERPProductionOrderItem {

	@ApiObjectField(required = true)
	private ERPProductionOrder productionOrder;
	@ApiObjectField(required = true)
	private ERPProduct product;
	@ApiObjectField(required = true)
	private Integer quantity;
	@ApiObjectField(required = true)
	private Integer itemId;

	@ApiObjectField(required = true)
	private ERPProcess process;
	@ApiObjectField(required = true)
	private ERPBom bom;

	private ERPBom oldBom;

	private Set<ERPProductionItemBom> itemBoms =new HashSet<ERPProductionItemBom>();


	@Id
	@SequenceGenerator(name = "ITEM_ID_GEN", sequenceName = "ITEM_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_ID_GEN")
	@Column(name = "ITEM_ID")
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", nullable = false)
	public ERPProductionOrder getProductionOrder() {
		return productionOrder;
	}

	public void setProductionOrder(ERPProductionOrder productionOrder) {
		this.productionOrder = productionOrder;
	}

	@ManyToOne
	@JoinColumn(name="PRODUCT_ID",nullable = false)
	public ERPProduct getProduct() {
		return product;
	}

	public void setProduct(ERPProduct product) {
		this.product = product;
	}


	@Column(name = "QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@ManyToOne
	@JoinColumn(name="PROCESS",nullable = false)
	public ERPProcess getProcess() {
		return process;
	}

	public void setProcess(ERPProcess process) {
		this.process = process;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="BOM",nullable = false)
	public ERPBom getBom() {
		return bom;
	}

	public void setBom(ERPBom bom) {
		this.bom = bom;
	}

	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "productionOrderDetail", cascade = CascadeType.ALL)
	public Set<ERPProductionItemBom> getItemBoms() {
		return itemBoms;
	}

	public void setItemBoms(Set<ERPProductionItemBom> itemBoms) {
		this.itemBoms = itemBoms;
	}

	@Transient
	public ERPBom getOldBom() {
		return oldBom;
	}

	public void setOldBom(ERPBom oldBom) {
		this.oldBom = oldBom;
	}

}
