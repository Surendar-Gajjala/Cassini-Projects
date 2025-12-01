package com.cassinisys.tms.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by CassiniSystems on 20-10-2016.
 */
@Entity
@Table(name = "TMS_TRIP")
@PrimaryKeyJoinColumn(name = "TRIP_ID")
@ApiObject(group = "TMS")
public class TMSTrip extends CassiniObject {

	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRIP_DATE", nullable = false)
	@ApiObjectField(required = true)
	private Date date;

	@Column(name = "TRIP_NOTES", nullable = false)
	@ApiObjectField(required = true)
	private String notes;

	protected TMSTrip() {
		super(TMSObjectType.TRIP);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
