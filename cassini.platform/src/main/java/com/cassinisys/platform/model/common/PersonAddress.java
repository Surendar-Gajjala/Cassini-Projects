package com.cassinisys.platform.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 
 * @author reddy
 *
 */
@Entity
@Data
@Table(name = "PERSONADDRESS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonAddress implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PersonAddressId id;

	public PersonAddress() {
	}

	public PersonAddress(Integer personId, Integer addressId) {
		this.id = new PersonAddressId(personId, addressId);
	}

	public PersonAddress(PersonAddressId id) {
		this.id = id;
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
		PersonAddress other = (PersonAddress) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
