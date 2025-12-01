package com.cassinisys.erp.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ERP_MESSAGESENTTO")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "COMMON")
public class ERPMessageSentTo {

	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField(required = true)
	private Integer message;
	@ApiObjectField(required = true)
	private Integer person;

	@Id
	@SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
	@Column(name = "ROWID", nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "MESSAGE", nullable = false)
	public Integer getMessage() {
		return message;
	}

	public void setMessage(Integer message) {
		this.message = message;
	}

	@Column(name = "PERSON", nullable = false)
	public Integer getPerson() {
		return person;
	}

	public void setPerson(Integer person) {
		this.person = person;
	}

}
