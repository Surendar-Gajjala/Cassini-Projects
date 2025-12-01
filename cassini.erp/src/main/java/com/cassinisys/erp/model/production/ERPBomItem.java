package com.cassinisys.erp.model.production;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Entity
@Table(name = "ERP_BOMITEM")
@ApiObject(group = "PRODUCTION")
public class ERPBomItem {

	@ApiObjectField(required = true)
	private Integer rowId;
	@ApiObjectField(required = true)
	private ERPBom bom;
	@ApiObjectField(required = true)
	private BomItemType itemType;
	@ApiObjectField(required = true)
	private Integer itemId;
	@ApiObjectField(required = true)
	private Integer quantity;

	private String bomItemObj;


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
	@JoinColumn(name = "BOM",nullable = false)
	public ERPBom getBom() {
		return bom;
	}

	public void setBom(ERPBom bom) {
		this.bom = bom;
	}


	@Column(name = "ITEM_TYPE")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType",
			parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
					value = "com.cassinisys.erp.model.production.BomItemType") })
	public BomItemType getItemType() {
		return itemType;
	}
	public void setItemType(BomItemType itemType) {
		this.itemType = itemType;
	}

	@Column(name = "ITEM_ID")
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@Column(name = "QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Transient
	public String getBomItemObj() {
		return bomItemObj;
	}

	public void setBomItemObj(String bomItemObj) {
		this.bomItemObj = bomItemObj;
	}


	}
