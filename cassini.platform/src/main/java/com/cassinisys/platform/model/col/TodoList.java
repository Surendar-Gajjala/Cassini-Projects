package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author reddy
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "TODOLIST")
@PrimaryKeyJoinColumn(name = "TODOLIST_ID")
public class TodoList extends CassiniObject {

	private static final long serialVersionUID = 1L;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	public TodoList() {
		super(ObjectType.TODOLIST);
	}



}
