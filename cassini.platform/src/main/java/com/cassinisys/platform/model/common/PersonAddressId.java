package com.cassinisys.platform.model.common;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class PersonAddressId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PERSON_ID", nullable = false)
	private Integer personId;

	@Column(name = "ADDRESS_ID", nullable = false)
	private Integer addressId;

	public PersonAddressId() {
	}

	public PersonAddressId(Integer personId, Integer addressId) {
		this.personId = personId;
		this.addressId = addressId;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((addressId == null) ? 0 : addressId.hashCode());
		result = prime * result
				+ ((personId == null) ? 0 : personId.hashCode());
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
		PersonAddressId other = (PersonAddressId) obj;
		if (addressId == null) {
			if (other.addressId != null)
				return false;
		} else if (!addressId.equals(other.addressId))
			return false;
		if (personId == null) {
			if (other.personId != null)
				return false;
		} else if (!personId.equals(other.personId))
			return false;
		return true;
	}

}
