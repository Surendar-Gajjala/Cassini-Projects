package com.cassinisys.platform.model.common;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by reddy on 7/18/15.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "ADDRESS")
@PrimaryKeyJoinColumn(name = "ADDRESS_ID")
public class Address extends CassiniObject {

	private static final long serialVersionUID = 1L;

	@Column(name = "ADDRESS_TYPE", nullable = false)
	private Integer addressType;

	@Column(name = "ADDRESS_TEXT")
	private String addressText;
	
	@Column(name = "DISTRICT")
	private String district;
	
	@Column(name = "PINCODE")
	private String pincode;

	
	@Column(name = "CITY")
	private String city;

	@Column(name = "STATE", nullable = false)
	private Integer state;

	@Column(name = "COUNTRY", nullable = false)
	private Integer country;

	public Address() {
		super(ObjectType.ADDRESS);
	}



}
