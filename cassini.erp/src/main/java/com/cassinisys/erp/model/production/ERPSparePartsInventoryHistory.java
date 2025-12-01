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
@Table(name = "ERP_SPAREPARTSINVENTORYHISTORY")
@ApiObject(group = "PRODUCTION")
public class ERPSparePartsInventoryHistory {

	
	/* ROWID                       INTEGER             NOT NULL PRIMARY KEY,
	    PART_ID                     INTEGER             NOT NULL REFERENCES ERP_MACHINESPAREPART (PART_ID),
	    IN_OR_OUT                   INVENTORYRECORD_TYPE          NOT NULL,
	    QUANTITY                    INTEGER             NOT NULL,
	    TIMESTAMP                   TIMESTAMP           NOT NULL,
	    EMPLOYEE                    INTEGER             NOT NULL REFERENCES ERP_EMPLOYEE (EMPLOYEE_ID)
	    */
	    @ApiObjectField(required = true)
		private Integer employee;
		@ApiObjectField(required = true)
		private Integer partId;
		@ApiObjectField(required = true)
		private Integer quantity;
		@ApiObjectField(required = true)
		private Integer rowId;
		@ApiObjectField(required = true)
		private Date timestamp;
		@ApiObjectField(required = true)
		private InventoryRecordType inOrOut;
		
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
		
		
		@Column(name = "IN_OR_OUT")
		@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.InventoryRecordType") })		
		public InventoryRecordType getInOrOut() {
			return inOrOut;
		}
		public void setInOrOut(InventoryRecordType inOrOut) {
			this.inOrOut = inOrOut;
		}
		
		
		
		
		
		@Column(name = "EMPLOYEE", nullable = false)
		public Integer getEmployee() {
			return employee;
		}
		
		public void setEmployee(Integer employee) {
			this.employee = employee;
		}
		
		@Column(name = "PART_ID", nullable = false)
		public Integer getPartId() {
			return partId;
		}
		public void setPartId(Integer partId) {
			this.partId = partId;
		}
		
		@Column(name = "QUANTITY", nullable = false)
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}		
		
		@Column(name = "TIMESTAMP", nullable = false)
		@Temporal(TemporalType.TIMESTAMP)
		@JsonSerialize(using = CustomDateSerializer.class)
		public Date getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Date timestamp) {
			this.timestamp = timestamp;
		}
		
}
