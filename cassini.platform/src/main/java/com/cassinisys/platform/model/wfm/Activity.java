package com.cassinisys.platform.model.wfm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lakshmi on 1/31/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "Activity")
//@PrimaryKeyJoinColumn(name = "ID")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "OBJECT_ID_GEN", sequenceName = "OBJECT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OBJECT_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;


    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Column(name = "SELECTED_ACTION")
    private Integer selectedAction;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.wfm.ActivityType")})
    @Column(name = "TYPE", nullable = true)
    private ActivityType type;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.wfm.ActivityStatus")})
    @Column(name = "STATUS", nullable = true)
    private ActivityStatus status = ActivityStatus.PENDING;

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

    @Transient
    private List<Action> actions = new ArrayList();

    @Transient
    private List<WorkflowTask> tasks = new ArrayList();


    public Activity() {
        //super(ObjectType.ACTIVITY);
    }

}
