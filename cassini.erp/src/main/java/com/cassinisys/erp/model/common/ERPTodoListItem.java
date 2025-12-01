package com.cassinisys.erp.model.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ERP_TODOLISTITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "COMMON")
public class ERPTodoListItem implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField(required = true)
	private Integer todoList;
	@ApiObjectField(required = true)
	private String text;
	@ApiObjectField
	private boolean completed;
	@ApiObjectField(required = true)
	private Date completedDate;

	@Id
	@SequenceGenerator(name = "TODOITEM_ID_GEN", sequenceName = "TODOITEM_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TODOITEM_ID_GEN")
	@Column(name = "TODOITEM_ID", nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TODO_LIST", nullable = false)
	public Integer getTodoList() {
		return todoList;
	}

	public void setTodoList(Integer todoList) {
		this.todoList = todoList;
	}

	@Column(name = "TEXT", nullable = false)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "COMPLETED", nullable = false)
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COMPLETED_DATE")
	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

}
