package com.cassinisys.erp.model.production;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;

@Entity
@Table(name = "ERP_WORKCENTER")
@PrimaryKeyJoinColumn(name = "WORKCENTER_ID")
@ApiObject(group = "PRODUCTION")
public class ERPWorkCenter extends ERPObject {
	   
		@ApiObjectField
		private String description;
		@ApiObjectField(required = true)
		private String name;
		
		public ERPWorkCenter() {
			super.setObjectType(ObjectType.WORKCENTER);
		}
		
		
		public String getDescription() {
			return description;
		}


		public void setDescription(String description) {
			this.description = description;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}
	
		
}
