package com.cassinisys.erp.model.production;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;

@Entity
@Table(name = "ERP_PROCESS")
@PrimaryKeyJoinColumn(name = "PROCESS_ID")
@ApiObject(group = "PRODUCTION")
public class ERPProcess extends ERPObject {
	  
	    @ApiObjectField
		private String description;
		@ApiObjectField(required = true)
		private String name;
		
		public ERPProcess(){
			super.setObjectType(ObjectType.PROCESS);
		}
		
		@Column(name = "DESCRIPTION")
		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		@Column(name = "NAME", nullable = false)
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		
}
