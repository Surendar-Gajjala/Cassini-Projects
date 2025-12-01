package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WBSELEMENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWbsElement extends CassiniObject {

    @Transient
    Double percentComplete = 0.0;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROJECT")
    private PLMProject project;
    @Column(name = "PARENT")
    private Integer parent;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "SEQUENCE_NUMBER")
    private Integer sequenceNumber;
    @Transient
    private Boolean hasBom;
    @Transient
    private Integer level = 0;
    @Transient
    private Boolean expanded = (Boolean.FALSE);
    @Transient
    private List<PLMActivity> activities = new ArrayList<>();

    @Transient
    private List<PLMMilestone> milestones = new ArrayList<>();

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_FINISHDATE")
    private Date plannedFinishDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_STARTDATE")
    private Date plannedStartDate;

    @Transient
    private Long ganttId;

    public PLMWbsElement() {
        super(PLMObjectType.PROJECTPHASEELEMENT);
    }


}
