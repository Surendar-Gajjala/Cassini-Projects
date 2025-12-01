package com.cassinisys.platform.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "PRIVILEGE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Privilege {
	
	@Id
	@SequenceGenerator(name = "PRIVILEGE_ID_GEN", sequenceName = "PRIVILEGE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRIVILEGE_ID_GEN")
	@Column(name = "PRIVILEGE_ID")
	private Integer id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;


}
