package com.cassinisys.platform.model.col;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author reddy
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "RECURRINGMEETING")
@PrimaryKeyJoinColumn(name = "MEETING_ID")
public class RecurringMeeting extends Meeting {

	private static final long serialVersionUID = 1L;

		
	@Type(type = "com.cassinisys.platform.util.EnumUserType",
			parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",	
			value = "com.cassinisys.platform.model.col.MeetingFrequencyType") })
	@Column(name = "FREQUENCY", nullable = false)
	private MeetingFrequencyType frequency;

	public RecurringMeeting() {
	}



}
