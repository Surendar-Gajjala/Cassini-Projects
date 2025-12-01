package com.cassinisys.erp.model.production;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.Set;

@Entity
@Table(name = "ERP_BOM")
@ApiObject(group = "PRODUCTION")
public class ERPBom {

	@ApiObjectField(required = true)
	private Integer bomId;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField(required = true)
	private BomType type;
	@ApiObjectField(required = true)
	private Integer objId;	
	private String description;

	private String bomObj;

	private Set<ERPBomItem> bomItems;
	
	@Id
	@SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
	@Column(name = "BOMID")
	public Integer getBomId() {
		return bomId;
	}
	public void setBomId(Integer bomId) {
		this.bomId = bomId;
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
	
	@Column(name = "TYPE")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType",
			parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
					value = "com.cassinisys.erp.model.production.BomType") })
	public BomType getType() {
		return type;
	}
	public void setType(BomType type) {
		this.type = type;
	}
	
	@Column(name = "OBJ_ID")
	public Integer getObjId() {
		return objId;
	}
	public void setObjId(Integer objId) {
		this.objId = objId;
	}

	@Transient
	public String getBomObj() {
		return bomObj;
	}

	public void setBomObj(String bomObj) {
		this.bomObj = bomObj;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "bom", cascade = CascadeType.ALL)
	public Set<ERPBomItem> getBomItems() {
		return bomItems;
	}

	public void setBomItems(Set<ERPBomItem> bomItems) {
		this.bomItems = bomItems;
	}
}
