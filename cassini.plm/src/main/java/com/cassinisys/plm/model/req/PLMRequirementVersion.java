package com.cassinisys.plm.model.req;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.*;

/**
 * Created by CassiniSystems on 10-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTVERSION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementVersion extends PLMRequirementObject {

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "MASTER")
    private PLMRequirement master;

    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "LATEST")
    private Boolean latest = Boolean.TRUE;

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Column(name = "PRIORITY")
    private String priority;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_FINISH_DATE")
    private Date plannedFinishDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_FINISH_DATE")
    private Date actualFinishDate;

    @Column(name = "NOTES")
    private String notes;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "DOCUMENT")
    private PLMRequirementDocumentRevision requirementDocumentRevision;

    @Column(name = "PARENT")
    private Integer parent;

    @Transient
    private List<PLMRequirementVersion> children = new ArrayList<>();

    @Transient
    private List<PLMRequirementVersionStatusHistory> statusHistory = new ArrayList<>();

    @Transient
    private PLMRequirementReviewer reviewer;

    @Transient
    private String comment;

    @Transient
    private Integer reqChild;

    @Transient
    private Integer reqDoc;

    @Transient
    private Integer workflowDefId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "requirementVersion", cascade = CascadeType.ALL)
    @OrderBy("ID ASC")
    private Set<PLMRequirementReviewer> reviewers = new HashSet<>();
}
