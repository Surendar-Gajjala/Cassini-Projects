package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.converter.LocalDateConverter;
import com.cassinisys.platform.util.converter.LocalTimeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author reddy
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "MEETING")
@PrimaryKeyJoinColumn(name = "MEETING_ID")
public class Meeting extends CassiniObject {

	private static final long serialVersionUID = 1L;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	@Column(name = "START_DATE", nullable = false)
    @Convert (converter = LocalDateConverter.class)
	private LocalDate startDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="hh:mm a")
	@Column(name = "START_TIME", nullable = false)
    @Convert (converter = LocalTimeConverter.class)
	private LocalTime startTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    @Column(name = "END_DATE", nullable = false)
    @Convert (converter = LocalDateConverter.class)
    private LocalDate endDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="hh:mm a")
	@Column(name = "END_TIME", nullable = false)
    @Convert (converter = LocalTimeConverter.class)
	private LocalTime endTime;

//	@OneToMany(fetch = FetchType.EAGER, mappedBy = "meeting", cascade=CascadeType.MERGE )
	@Transient
	private List<Person> attendees = new ArrayList<>();

	@Column(name = "MINUTES", nullable = false)
	private String minutes;

	public Meeting() {
		super(ObjectType.MEETING);
	}


}
