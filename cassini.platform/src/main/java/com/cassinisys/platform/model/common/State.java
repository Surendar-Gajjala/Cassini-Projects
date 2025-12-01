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
@Table(name = "STATE")
@PrimaryKeyJoinColumn(name = "STATE_ID")
public class State extends CassiniObject {

	private static final long serialVersionUID = 1L;

	@Column(name = "COUNTRY", nullable = false)
	private Integer country;

	@Column(name = "NAME", nullable = false, unique = true)
	private String name;

	@Column(name = "SHORT_NAME")
	private String shortName;

	public State() {
		super(ObjectType.STATE);
	}


}
