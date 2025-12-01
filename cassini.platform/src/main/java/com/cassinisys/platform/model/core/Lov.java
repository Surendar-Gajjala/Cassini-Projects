package com.cassinisys.platform.model.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

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
@Table(name = "LOV")
public class Lov implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "LOV_ID_GEN",
			sequenceName = "LOV_ID_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "LOV_ID_GEN")
	@Column(name = "LOV_ID", nullable = false)
	private Integer id;

	@Column(name = "TYPE")
	String type;

	@Column(name = "NAME", nullable = false)
	String name;

	@Column(name = "DESCRIPTION", nullable = false)
	String description;

	@Column(name = "VALUES", nullable = false)
	@Type(type = "com.cassinisys.platform.util.converter.StringArrayType")
	String[] values;

	@Column(name = "DEFAULT_VALUE")
	String defaultValue;

	@Transient
	private Boolean usedLov = Boolean.FALSE;

	public Lov() {
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
		Lov other = (Lov) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
