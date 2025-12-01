package com.cassinisys.erp.model.production;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_PRODUCTTYPE")
@ApiObject(group = "PRODUCTION")
public class ERPProductType {

	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField
	private String description;

	@Id
	@SequenceGenerator(name = "PRODUCTTYPE_ID_GEN", sequenceName = "PRODUCTTYPE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTTYPE_ID_GEN")
	@Column(name = "TYPE_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
}
