package com.cassinisys.platform.model.common;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "DISTRICT")
@PrimaryKeyJoinColumn(name = "DISTRICT_ID")
public class District extends CassiniObject {

	@Column(name = "STATE")
	private Integer state;

	@Column(name = "NAME")
	private String name;

	public District() {
		super(ObjectType.DISTRICT);
	}

}
