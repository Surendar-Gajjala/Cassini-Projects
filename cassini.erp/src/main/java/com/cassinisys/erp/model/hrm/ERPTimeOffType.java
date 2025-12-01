package com.cassinisys.erp.model.hrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ERP_TIMEOFFTYPE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "HRM")
public class ERPTimeOffType implements Serializable {

	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField(required = true)
	private String typeCode;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField
	private String description;

	@Id
	@SequenceGenerator(name = "TIMEOFFTYPE_ID_GEN", sequenceName = "TIMEOFFTYPE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIMEOFFTYPE_ID_GEN")
	@Column(name = "TYPE_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TYPE_CODE")
	public String getTypeCode() {
		return typeCode;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
