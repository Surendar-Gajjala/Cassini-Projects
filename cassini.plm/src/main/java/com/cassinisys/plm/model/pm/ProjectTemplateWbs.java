package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by subramanyam reddy on 14-03-2018.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PROJECT_TEMPLATEWBS")
@PrimaryKeyJoinColumn(name = "ID")
public class ProjectTemplateWbs extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TEMPLATE")
    private Integer template;

    @Column(name = "SEQUENCE_NUMBER")
    private Integer sequenceNumber;

    @Transient
    private Boolean hasBom;

    @Transient
    private Integer level = 0;

    @Transient
    private Boolean expanded = (Boolean.FALSE);

    @Transient
    private List<ProjectTemplateActivity> templateActivities;

    @Transient
    private List<ProjectTemplateMilestone> templateMilestones;

    @Transient
    private Long ganttId;

    public ProjectTemplateWbs() {
        super(PLMObjectType.TEMPLATEPHASE);
    }

}

