package com.cassinisys.is.model.pm;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by Rajabrahmachary on 07-06-2016.
 */

@Entity
@Table(name = "IS_PROJECTRESOURCE")
@PrimaryKeyJoinColumn(name = "RESOURCE_ID")
@ApiObject(name = "PM")
public class ISProjectResource extends CassiniObject {

    @Column(name = "REFERENCE_ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer referenceId;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    @Column(name = "TASK", nullable = false)
    @ApiObjectField(required = true)
    private Integer task;

    @Column(name = "QUANTITY", nullable = false)
    @ApiObjectField(required = true)
    private Double quantity = 0.0;

    @Column(name = "UNITS", nullable = false)
    @ApiObjectField(required = true)
    private String units;

    @Column(name = "ISSUED_QUANTITY")
    private Double issuedQuantity = 0.0;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.pm.ResourceType")})
    @Column(name = "RESOURCETYPE", nullable = false)
    @ApiObjectField(required = true)
    private ResourceType resourceType;

    public ISProjectResource() {
        super(ISObjectType.RESOURCE);
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getIssuedQuantity() {
        return issuedQuantity;
    }

    public void setIssuedQuantity(Double issuedQuantity) {
        this.issuedQuantity = issuedQuantity;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }
}
