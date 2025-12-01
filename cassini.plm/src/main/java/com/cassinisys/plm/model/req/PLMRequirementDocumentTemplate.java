package com.cassinisys.plm.model.req;

import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTDOCUMENTTEMPLATE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementDocumentTemplate extends PLMRequirementObject {

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PLMRequirementDocumentType type;


    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @Transient
    private Boolean documentReviewer = Boolean.FALSE;

    @Transient
    private Boolean requirementReviewer = Boolean.FALSE;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "documentTemplate", cascade = CascadeType.ALL)
    @OrderBy("ID ASC")
    private Set<PLMRequirementDocumentTemplateReviewer> reviewers = new HashSet<>();


    public PLMRequirementDocumentTemplate() {
        super.setObjectType(RequirementEnumObject.REQUIREMENTDOCUMENTTEMPLATE);
    }
}

