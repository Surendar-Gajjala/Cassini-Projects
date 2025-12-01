package com.cassinisys.platform.model.col;

import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author reddy
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "TODOITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TodoItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TODOITEM_ID_GEN",
			sequenceName = "TODOITEM_ID_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "TODOITEM_ID_GEN")
	@Column(name = "TODOITEM_ID", nullable = false)
	private Integer id;

	@Column(name = "TODO_LIST", nullable = false)
	private Integer todoList;

	@Column(name = "COMPLETED", nullable = false)
	private boolean completed;

	@JsonSerialize(using = CustomDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COMPLETED_DATE")
	private Date completedDate;

	public TodoItem() {
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TodoItem other = (TodoItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
