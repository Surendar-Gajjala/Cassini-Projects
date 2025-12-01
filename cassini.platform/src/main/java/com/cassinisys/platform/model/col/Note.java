package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

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
@Table(name = "NOTE")
@PrimaryKeyJoinColumn(name = "NOTE_ID")
public class Note extends CassiniObject {

	private static final long serialVersionUID = 1L;
	 
	@Type(type = "com.cassinisys.platform.util.EnumUserType",
			parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
					value = "com.cassinisys.platform.model.core.ObjectType") })
	@Column(name = "OBJECT_TYPE", nullable = false)
	private ObjectType objectType;

	@Column(name = "OBJECT_ID", nullable = false)
	private Integer objectId;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "DETAILS")
	private String details;

	public Note() {
		super(ObjectType.NOTE);
	}


}
