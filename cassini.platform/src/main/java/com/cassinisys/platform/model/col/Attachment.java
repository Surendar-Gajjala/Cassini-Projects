package com.cassinisys.platform.model.col;

import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author reddy
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "ATTACHMENT")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment implements Serializable ,Cloneable{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ATTACHMENT_ID_GEN",
			sequenceName = "ATTACHMENT_ID_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "ATTACHMENT_ID_GEN")

	@Column(name = "ATTACHMENT_ID", nullable = false)
	private Integer id;
	@Type(type = "com.cassinisys.platform.util.EnumUserType",
			parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
					value = "com.cassinisys.platform.model.core.ObjectType") })
	@Column(name = "OBJECT_TYPE", nullable = false)
	private Enum objectType;

	@Column(name = "OBJECT_ID", nullable = false)
	private Integer objectId;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "EXTENSION", nullable = false)
	private String extension;

	@Column(name = "SIZE", nullable = false)
	private Integer size;

	@JsonSerialize(using = CustomDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ADDED_ON", nullable = false)
	private Date addedOn;

	@Column(name = "ADDED_BY", nullable = false)
	private Integer addedBy;

	public Attachment() {
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
		Attachment other = (Attachment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
