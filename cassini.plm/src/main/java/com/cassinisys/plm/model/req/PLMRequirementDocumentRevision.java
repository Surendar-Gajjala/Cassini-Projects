package com.cassinisys.plm.model.req;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by CassiniSystems on 10-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTDOCUMENTREVISION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementDocumentRevision extends PLMRequirementObject {


    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "MASTER")
    private PLMRequirementDocument master;

    @Column(name = "REVISION")
    private String revision;

    @Column(name = "LATEST")
    private Boolean latest = Boolean.FALSE;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER")
    private Person owner;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private PLMRequirementDocumentReviewer reviewer;

    @Transient
    private Boolean requirementApproveButton = Boolean.FALSE;

    @Transient
    private String comment;

    @Transient
    private String workflowStatus="None";

    @Transient
    private Integer workflowDefId;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private Boolean finishWorkflow = Boolean.FALSE;

    @Transient
    private List<PLMRequirementDocumentRevisionStatusHistory> statusHistory = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "requirementDocumentRevision", cascade = CascadeType.ALL)
    @OrderBy("ID ASC")
    private Set<PLMRequirementDocumentReviewer> reviewers = new HashSet<>();

    public PLMRequirementDocumentRevision() {
        super.setObjectType(RequirementEnumObject.REQUIREMENTDOCUMENTREVISION);
    }
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";
}
