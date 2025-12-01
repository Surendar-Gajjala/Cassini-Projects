package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by CassiniSystems on 16-05-2019.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PROJECT_TEMPLATETASK")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectTemplateTask extends CassiniObject {

    @Column
    private Integer activity;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Column(name = "SEQUENCE_NUMBER")
    private Integer sequenceNumber;

    @Column(name = "WORKFLOW")
    private Integer workflow;
    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private Boolean finishWorkflow = Boolean.FALSE;

    @Transient
    private Long ganttId;

    public ProjectTemplateTask() {
        super(PLMObjectType.TEMPLATETASK);
    }


}
