package com.cassinisys.erp.model.hrm;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.converters.CustomShortDateDeserializer;
import com.cassinisys.erp.converters.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public	class AttendanceDTO implements Comparable<AttendanceDTO>{
		
		private Date inTime;
		private Date outTime;	
		private Date date;
		private AttendanceStatus status=AttendanceStatus.A;
		
		@Temporal(TemporalType.TIMESTAMP)
		@JsonSerialize(using = CustomDateSerializer.class)
		public Date getInTime() {
			return inTime;
		}
		public void setInTime(Date inTime) {
			this.inTime = inTime;
		}
		
		@Temporal(TemporalType.TIMESTAMP)
		@JsonSerialize(using = CustomDateSerializer.class)
		public Date getOutTime() {
			return outTime;
		}
		public void setOutTime(Date outTime) {
			this.outTime = outTime;
		}
		@Temporal(TemporalType.TIMESTAMP)
		@JsonDeserialize(using= CustomShortDateDeserializer.class)
		@JsonSerialize(using = CustomShortDateSerializer.class)
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public AttendanceStatus getStatus() {
			return status;
		}
		public void setStatus(AttendanceStatus status) {
			this.status = status;
		}

	@Override
	public int compareTo(AttendanceDTO o) {
		return this.getDate().compareTo(o.getDate());
	}
	}
