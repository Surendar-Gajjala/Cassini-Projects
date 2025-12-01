package com.cassinisys.is.model.procm;
/* Model for ISRequisitionRequest */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_REQUISITIONREQUEST")
@PrimaryKeyJoinColumn(name = "REQUEST_ID")
@ApiObject(name = "PROCM")
public class ISRequisitionRequest extends CassiniObject {

    private static final long serialVersionUID = 1L;

    public ISRequisitionRequest() {
        super(ISObjectType.REQUISITION);
    }
}
