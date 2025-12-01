package com.cassinisys.pdm.model;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 20-01-2017.
 */
@Embeddable
public class PDMItemAttributeId implements Serializable {


	private static final long serialVersionUID = 1L;

	@Column(name = "ITEM", nullable = false)
	@ApiObjectField(required = true)
	private Integer item;

	@Column(name = "ATTRIBUTE", nullable = false)
	@ApiObjectField(required = true)
	private Integer attribute;

	public PDMItemAttributeId() {
	}

	public PDMItemAttributeId(Integer item, Integer attribute) {
		this.item = item;
		this.attribute = attribute;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public Integer getAttribute() {
		return attribute;
	}

	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result
				+ ((item == null) ? 0 : item.hashCode());
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
		PDMItemAttributeId other = (PDMItemAttributeId) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		return true;

	}
}
