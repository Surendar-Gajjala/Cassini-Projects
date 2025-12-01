package com.cassinisys.erp.model.production;

import com.cassinisys.erp.util.LocalTimeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "ERP_WORKSHIFT")
@ApiObject(group = "PRODUCTION")
public class ERPWorkShift {
	

	@ApiObjectField(required = true)
	private LocalTime startTime;

	@ApiObjectField(required = true)
	private LocalTime endTime;

	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField(required = true)
	private Integer shiftId;

	@Id
	@SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
	@Column(name = "SHIFT_ID")
	public Integer getShiftId() {
			return shiftId;
		}

	public void setShiftId(Integer shiftId) {
			this.shiftId = shiftId;
		}

	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="hh:mm a")
	@Column(name = "START_TIME", nullable = false)
	@Convert (converter = LocalTimeConverter.class)
	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="hh:mm a")
	@Column(name = "END_TIME", nullable = false)
	@Convert(converter = LocalTimeConverter.class)
	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}


	@Column(name = "NAME")
	public String getName() {
			return name;
		}

	public void setName(String name) {
			this.name = name;
		}	

}
