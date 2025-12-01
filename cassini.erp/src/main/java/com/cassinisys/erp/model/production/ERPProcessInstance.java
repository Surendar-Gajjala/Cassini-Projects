package com.cassinisys.erp.model.production;

import com.cassinisys.erp.model.core.ERPObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyamReddy on 02-02-2017.
 */
@Entity
@Table(name = "ERP_PROCESSINSTANCE")
@PrimaryKeyJoinColumn(name = "INSTANCE_ID")
@ApiObject(group = "PRODUCTION")

public class ERPProcessInstance extends ERPObject{

	@Column(name = "NAME")
	private String name;

	@ApiObjectField(required = true)
	@Column(name = "DESCRIPTION")
	private String description;

	@ApiObjectField(required = true)
	@Column(name = "PRODUCTION_ORDER")
	private Integer productionOrder;

	@ApiObjectField(required = true)
	@Column(name = "ITEM")
	private Integer item;


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProductionOrder() {
		return productionOrder;
	}

	public void setProductionOrder(Integer productionOrder) {
		this.productionOrder = productionOrder;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}
}
