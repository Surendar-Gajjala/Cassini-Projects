package com.cassinisys.is.model.procm;
/**
 * Model for ISBoq
 */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_BOQ")
@PrimaryKeyJoinColumn(name = "BOQ_ID")
@ApiObject(name = "PROCM")
public abstract class ISBoq extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    @ApiObjectField
    private String name = "New Group";

    public ISBoq() {
        super(ISObjectType.BOQ);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
