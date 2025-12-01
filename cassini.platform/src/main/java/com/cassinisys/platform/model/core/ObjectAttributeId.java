package com.cassinisys.platform.model.core;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * 
 * @author reddy
 *
 */
@Embeddable
@Data
@EqualsAndHashCode(callSuper=false)
public class ObjectAttributeId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "OBJECT_ID")
	private Integer objectId;

	@Column(name = "ATTRIBUTEDEF")
	private Integer attributeDef;

	public ObjectAttributeId() {
	}

	public ObjectAttributeId(Integer objectId, Integer attributeDef) {
		this.objectId = objectId;
		this.attributeDef = attributeDef;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeDef == null) ? 0 : attributeDef.hashCode());
		result = prime * result
				+ ((objectId == null) ? 0 : objectId.hashCode());
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
		ObjectAttributeId other = (ObjectAttributeId) obj;
		if (attributeDef == null) {
			if (other.attributeDef != null)
				return false;
		} else if (!attributeDef.equals(other.attributeDef))
			return false;
		if (objectId == null) {
			if (other.objectId != null)
				return false;
		} else if (!objectId.equals(other.objectId))
			return false;
		return true;
	}

}
