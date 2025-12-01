package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by subramanyam reddy on 14-03-2018.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PROJECT_TEMPLATEMILESTONE")
@PrimaryKeyJoinColumn(name = "ID")
public class ProjectTemplateMilestone extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "WBS")
    private Integer wbs;

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Column(name = "SEQUENCE_NUMBER")
    private Integer sequenceNumber;

    @Transient
    private Integer level = 0;

    @Transient
    private Long ganttId;

    public ProjectTemplateMilestone() {
        super(PLMObjectType.TEMPLATEMILESTONE);
    }


}
