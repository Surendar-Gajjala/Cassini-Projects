package com.cassinisys.plm.model.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by CassiniSystems on 10-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTDOCUMENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementDocument extends PLMRequirementObject {


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PLMRequirementDocumentType type;

    @Column(name = "TEMPLATE")
    private Integer template;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "LATEST_REVISION")
    private Integer latestRevision;

    @Transient
    private Integer person;

    @Transient
    private Boolean documentReviewer = Boolean.FALSE;

    @Transient
    private Integer workflowDefId;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private Boolean requirementReviewer = Boolean.FALSE;

    public PLMRequirementDocument() {
        super.setObjectType(RequirementEnumObject.REQUIREMENTDOCUMENT);
    }
    @Transient
    private Boolean finishWorkflow = Boolean.FALSE;
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";
}
