package com.cassinisys.plm.model.req;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 10-11-2020.
 */
@Entity
@Table(name = "PLM_REQUIREMENTDOCUMENT_CHILDREN")
@PrimaryKeyJoinColumn(name = "ID")
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMRequirementDocumentChildren extends CassiniObject {

    @Column(name = "DOCUMENT")
    private Integer document;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUIREMENT_VERSION")
    private PLMRequirementVersion requirementVersion;

    @Column(name = "PARENT")
    private Integer parent;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Integer workflowDefId;

    @Transient
    private String workflowStatus="None";

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private Boolean finishWorkflow = Boolean.FALSE;

    @Transient
    private List<PLMRequirementDocumentChildren> children = new ArrayList<>();

    public PLMRequirementDocumentChildren() {
        super(PLMObjectType.REQUIREMENTDOCUMENTCHILDREN);
    }
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";

}
