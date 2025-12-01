package com.cassinisys.is.model.procm;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by Namratha on 17-01-2019.
 */
//public class ISWorkOrderItem {
//}
@Entity
@Table(name = "IS_WORKORDERITEM")
@PrimaryKeyJoinColumn(name = "ID")
@ApiObject(name = "IS_WORKORDERITEM")
public class ISWorkOrderItem extends CassiniObject {

    @Column(name = "WORK_ORDER", nullable = false)
    @ApiObjectField(required = true)
    private Integer workOrder;

    @Column(name = "TASK", nullable = false)
    @ApiObjectField(required = true)
    private Integer task;

    public ISWorkOrderItem() {
        super(ISObjectType.WORKORDERITEM);
    }

    public Integer getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(Integer workOrder) {
        this.workOrder = workOrder;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

}