package com.cassinisys.is.model.procm;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by Namratha on 17-01-2019.
 */

@Entity
@Table(name = "IS_WORKORDER")
@PrimaryKeyJoinColumn(name = "ID")
@ApiObject(name = "IS_WORKORDER")
public class ISWorkOrder extends CassiniObject {

    @Column(name = "NUMBER", nullable = false)
    @ApiObjectField(required = true)
    private String number;

    @Column(name = "CONTRACTOR", nullable = false)
    @ApiObjectField(required = true)
    private Integer contractor;

    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.procm.WorkOrderStatus")})
    @Column(name = "STATUS", nullable = false)
    private WorkOrderStatus status = WorkOrderStatus.PENDING;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    @Transient
    private ISProject isProject;

    @Transient
    private ISContractor isContractor;

    @Transient
    private Person person;

    public ISWorkOrder() {
        super(ISObjectType.WORKORDER);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getContractor() {
        return contractor;
    }

    public void setContractor(Integer contractor) {
        this.contractor = contractor;
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public ISProject getIsProject() {
        return isProject;
    }

    public void setIsProject(ISProject isProject) {
        this.isProject = isProject;
    }

    public ISContractor getIsContractor() {
        return isContractor;
    }

    public void setIsContractor(ISContractor isContractor) {
        this.isContractor = isContractor;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
