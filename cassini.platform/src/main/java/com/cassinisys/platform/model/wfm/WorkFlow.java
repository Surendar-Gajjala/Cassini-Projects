package com.cassinisys.platform.model.wfm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.ObjectTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lakshmi on 1/31/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "WORKFLOW")
@PrimaryKeyJoinColumn(name = "ID")
public class WorkFlow extends CassiniObject {

    private static final long serialVersionUID = 1L;
    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DEFINITION")
    private Integer definition;

    @Column(name = "DIAGRAM")
    private String diagram;

    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Column(name = "ATTACHED_OBJECT_ID")
    private Integer attachedObjId;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType")})
    @Column(name = "ATTACHED_OBJECT_TYPE", nullable = true)
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    private Enum attachedObjType;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.wfm.WorkFlowStatus")})
    @Column(name = "STATUS", nullable = true)
    private WorkFlowStatus status = WorkFlowStatus.PENDING;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    private Date startDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISH_DATE")
    private Date finishDate;

    @Column(name = "CURRENT_ACTIVITY")
    private Integer currentActivity;

    @Transient
    private List<Activity> activities = new ArrayList<>();


    public WorkFlow() {
        super(ObjectType.WORKFLOW);
    }

}

