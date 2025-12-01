package com.cassinisys.platform.model.wfm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "ACTIVITYTASK")
@PrimaryKeyJoinColumn(name = "ID")
public class WorkflowTask extends CassiniObject {

    @Column(name = "ACTIVITY")
    private Integer activity;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;


    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISH_DATE")
    private Date finishDate;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.wfm.WorkFlowTaskStatus")})
    @Column(name = "STATUS", nullable = true)
    private WorkFlowTaskStatus status = WorkFlowTaskStatus.PENDING;

    @Column(name = "OPTIONAL")
    private boolean optional;

    @Column(name = "NOTES")
    private String notes;

    public WorkflowTask() {
        super(ObjectType.TASK);
    }


}
