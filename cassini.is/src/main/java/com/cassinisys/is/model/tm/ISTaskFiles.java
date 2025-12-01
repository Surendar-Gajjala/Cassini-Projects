package com.cassinisys.is.model.tm;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Cassinisys on 18-05-2017.
 */
@Entity
@Table(name = "IS_TASKFILES")
@ApiObject(name = "TM")
public class ISTaskFiles implements Serializable {

    @Id
    @SequenceGenerator(name = "IS_TASKFILES_ID_GEN", sequenceName = "IS_TASKFILES_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IS_TASKFILES_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer rowId;

    @Column(name = "TASK")
    private Integer task;

    @Column(name = "TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.tm.AttachmentType")})
    private AttachmentType type;

    @Column(name = "REFITEM")
    private Integer refItem;

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

    public AttachmentType getType() {
        return type;
    }

    public void setType(AttachmentType type) {
        this.type = type;
    }

    public Integer getRefItem() {
        return refItem;
    }

    public void setRefItem(Integer refItem) {
        this.refItem = refItem;
    }
}
