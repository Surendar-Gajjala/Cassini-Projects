package com.cassinisys.is.model.tm;
/* Model for ISTaskResource  */

import com.cassinisys.is.model.pm.ResourceType;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IS_TASKRESOURCE")
@ApiObject(name = "TM")
public class ISTaskResource implements Serializable {

    @Id
    @SequenceGenerator(name = "TASKRESOURCE_ID_GEN", sequenceName = "TASKRESOURCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASKRESOURCE_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer rowId;

    @Column(name = "TASK")
    private Integer task;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.pm.ResourceType")})
    @Column(name = "RESOURCE_TYPE", nullable = false)
    @ApiObjectField(required = true)
    private ResourceType resourceType;

    @Column(name = " RESOURCE_ID ")
    @ApiObjectField(required = true)
    private Integer resourceId;

    @Column(name = "UNITS")
    private Integer units;

    @Column(name = "QUANTITY")
    private Double quantity = 0.0;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

}
