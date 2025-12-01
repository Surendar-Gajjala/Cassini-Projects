package com.cassinisys.erp.model.production;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ERP_WORKCENTERJOBSTATUSHISTORY")
@ApiObject(group = "PRODUCTION")
public class ERPWorkCenterJobStatusHistory {
		    
	    @ApiObjectField(required = true)
		private Integer modifiedBy;
		@ApiObjectField(required = true)
		private Integer job;		
		@ApiObjectField(required = true)
		private Integer rowId;
		@ApiObjectField(required = true)
		private Date modifiedDate;
		@ApiObjectField(required = true)
		private JobStatus newStatus;
		@ApiObjectField
		private JobStatus oldStatus;
		
		@Column(name = "MODIFIED_BY", nullable = false)
		public Integer getModifiedBy() {
			return modifiedBy;
		}
		public void setModifiedBy(Integer modifiedBy) {
			this.modifiedBy = modifiedBy;
		}
		
		@Column(name = "JOB", nullable = false)
		public Integer getJob() {
			return job;
		}
		public void setJob(Integer job) {
			this.job = job;
		}
		
		
		@Id
		@SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
		@Column(name = "ROWID")
		public Integer getRowId() {
			return rowId;
		}

		public void setRowId(Integer rowId) {
			this.rowId = rowId;
		}
		
		@Column(name = "MODIFIED_DATE", nullable = false)
		@Temporal(TemporalType.TIMESTAMP)
		@JsonSerialize(using = CustomDateSerializer.class)
		public Date getModifiedDate() {
			return modifiedDate;
		}
		public void setModifiedDate(Date modifiedDate) {
			this.modifiedDate = modifiedDate;
		}
		
		@Column(name = "NEW_STATUS", nullable = false)
		@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.JobStatus") })
		public JobStatus getNewStatus() {
			return newStatus;
		}
		
		public void setNewStatus(JobStatus newStatus) {
			this.newStatus = newStatus;
		}
		
		@Column(name = "OLD_STATUS")
		@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.JobStatus") })
		public JobStatus getOldStatus() {
			return oldStatus;
		}
		public void setOldStatus(JobStatus oldStatus) {
			this.oldStatus = oldStatus;
		}
		

}
