package com.cassinisys.platform.model.wfm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lakshmi on 1/31/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "ACTIVITYDEFINITION")
//@PrimaryKeyJoinColumn(name = "ID")
public class ActivityDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "OBJECT_ID_GEN", sequenceName = "OBJECT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OBJECT_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;


    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.wfm.ActivityType")})
    @Column(name = "TYPE", nullable = true)
    private ActivityType type = ActivityType.NORMAL;

    @Transient
    private List<ActionDefinition> actions = new ArrayList<>();

    @Transient
    private List<TaskDefinition> tasks = new ArrayList<>();

    @Transient
    private Integer instanceId;


    public ActivityDefinition() {
        //super(ObjectType.ACTIVITYDEFINITION);
    }

    public Activity createIntsance() {
        Activity activity = new Activity();
        activity.setId(null);
        //activity.setObjectType(ObjectType.ACTIVITY);
        activity.setName(this.getName());
        activity.setDescription(this.getDescription());
        activity.setDiagramId(this.getDiagramId());
        activity.setType(this.getType());
        return activity;
    }
}
