package com.cassinisys.platform.model.common;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "LOCATIONAWAREOBJECT")
@PrimaryKeyJoinColumn(name = "OBJECT_ID")
public class LocationAwareObject extends CassiniObject {

	public LocationAwareObject(Enum objectType) {
		super(objectType);
	}

	@JsonManagedReference
	@OneToOne(mappedBy = "object", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ObjectGeoLocation geoLocation;



}
