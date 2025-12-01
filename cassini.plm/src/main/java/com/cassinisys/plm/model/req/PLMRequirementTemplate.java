package com.cassinisys.plm.model.req;

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
 * Created by Lenovo on 25-06-2021.
 */
@Entity
@Data
@Table(name = "PLM_REQUIREMENTTEMPLATE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementTemplate extends PLMRequirementObject {

    @Column(name = "SEQUENCE", nullable = false)
    private Integer sequence;

    @Column(name = "PATH", nullable = false)
    private String path;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "DOCUMENT_TEMPLATE")
    private PLMRequirementDocumentTemplate documentTemplate;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PLMRequirementType type;

    @Column(name = "PARENT", nullable = false)
    private Integer parent;

/*    @Column(name = "NUMBER", nullable = false)
    private String number;*/

    @Column(name = "PRIORITY", nullable = false)
    private String priority;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Transient
    private List<PLMRequirementTemplate> children = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "requirementTemplate", cascade = CascadeType.ALL)
    @OrderBy("ID ASC")
    private Set<PLMRequirementTemplateReviewer> reviewers = new HashSet<>();

    public PLMRequirementTemplate() {
        super.setObjectType(RequirementEnumObject.REQUIREMENTTEMPLATE);
    }

}
