package com.cassinisys.erp.model.production;

import javax.persistence.*;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_MATERIALCATEGORY")
@ApiObject(group = "PRODUCTION")
public class ERPMaterialCategory {

	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField(required = true)
	private ERPMaterialType type;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField(required = true)
	private String description;
	@ApiObjectField
	private Integer parent;

	@ApiObjectField
	private List<ERPMaterialCategory> children = new ArrayList<>();


	@Id
	@SequenceGenerator(name = "MATERIALCATEGORY_ID_GEN", sequenceName = "MATERIALCATEGORY_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MATERIALCATEGORY_ID_GEN")
	@Column(name = "CATEGORY_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MATERIAL_TYPE")
	public ERPMaterialType getType() {
		return type;
	}

	public void setType(ERPMaterialType type) {
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

	@Column(name = "PARENTCATEGORY")
	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	@Transient
	public List<ERPMaterialCategory> getChildren() {
		return children;
	}

	public void setChildren(List<ERPMaterialCategory> children) {
		this.children = children;
	}
}
