package com.cassinisys.is.model.procm;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Namratha on 17-01-2019.
 */

@Entity
@Table(name = "IS_CONTRACTOR")
@PrimaryKeyJoinColumn(name = "ID")
@ApiObject(name = "PROCM")
public class ISContractor extends CassiniObject {

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "CONTACT", nullable = false)
    @ApiObjectField(required = true)
    private Integer contact;

    @Column(name = "ACTIVE", nullable = false)
    @ApiObjectField(required = true)
    private Boolean active;

    @Transient
    private List<ISWorkOrder> workOrders;

    public ISContractor() {
        super(ISObjectType.CONTRACTOR);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ISWorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<ISWorkOrder> workOrders) {
        this.workOrders = workOrders;
    }
}

