package com.cassinisys.platform.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 
 * @author reddy
 *
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "ADDRESSTYPE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ADDRESSTYPE_ID_GEN",
			sequenceName = "ADDRESSTYPE_ID_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "ADDRESSTYPE_ID_GEN")
	@Column(name = "TYPE_ID", nullable = false)
	private Integer id;

	@Column(name = "NAME", nullable = false, unique = true)
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	public AddressType() {
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
		AddressType other = (AddressType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
