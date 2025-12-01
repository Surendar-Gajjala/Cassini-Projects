package com.cassinisys.platform.model.common;

import com.cassinisys.platform.model.core.ObjectType;
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
@Table(name = "TAG")
public class Tag implements Serializable {

	@Id
	@SequenceGenerator(name = "TAG_ID_GEN", sequenceName = "TAG_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TAG_ID_GEN")
	@Column(name = "TAG_ID")
	private Integer id;

	@Column(name = "LABEL", nullable = false)
	private String label;

	@Type(type = "com.cassinisys.platform.util.EnumUserType",
			parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
					value = "com.cassinisys.platform.model.core.ObjectType") })
	@Column(name = "OBJECT_TYPE", nullable = false)
	private ObjectType objectType;

	@Column(name = "OBJECT", nullable = false)
	private Integer object;


}
