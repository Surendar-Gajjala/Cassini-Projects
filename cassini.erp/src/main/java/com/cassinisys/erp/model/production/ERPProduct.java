package com.cassinisys.erp.model.production;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.cassinisys.erp.model.hrm.ERPBusinessUnit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by reddy on 7/18/15.
 */
@Entity
@Table(name = "ERP_PRODUCT")
@PrimaryKeyJoinColumn(name = "PRODUCT_ID")
@ApiObject(group = "PRODUCTION")
public class ERPProduct extends ERPObject {

	@ApiObjectField
	private ERPBusinessUnit businessUnit;
	@ApiObjectField(required = true)
	private String sku;
	@ApiObjectField(required = true)
	private ERPProductCategory category;
	@ApiObjectField(required = true)
	private Integer type;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField
	private String description;
	@ApiObjectField
	private byte[] picture;
	@ApiObjectField(required = true)
	private ProductStatus status = ProductStatus.ACTIVE;
	@ApiObjectField
	private String units = "EA";
	@ApiObjectField
	private Double unitPrice = 0.0;

	public ERPProduct() {
		super.setObjectType(ObjectType.PRODUCT);
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BUSINESS_UNIT")
	public ERPBusinessUnit getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(ERPBusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

	@Column(name = "SKU")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CATEGORY", nullable = false)
	public ERPProductCategory getCategory() {
		return category;
	}

	public void setCategory(ERPProductCategory category) {
		this.category = category;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	@Column(name = "PICTURE")
	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	@Column(name = "STATUS")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.ProductStatus") })
	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

	@Column(name = "UNITS")
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Column(name = "UNITPRICE")
	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
}
