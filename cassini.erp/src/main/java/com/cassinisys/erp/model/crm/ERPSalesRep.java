package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.model.core.ObjectType;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ERP_SALESREP")
@PrimaryKeyJoinColumn(name = "SALESREP_ID")
@ApiObject(group = "CRM")
public class ERPSalesRep extends ERPEmployee {
    public ERPSalesRep() {
        super.setObjectType(ObjectType.SALESREP);
    }
}
