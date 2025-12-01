package com.cassinisys.plm.model.rm;

import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.PLMSubscribe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "RM_SPECIFICATION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Specification extends RmObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private SpecificationType type;

    @Transient
    private Integer totalReqs;

    @Transient
    private Integer pending;

    @Transient
    private Integer finished;

    @Transient
    private Integer none;

    @Column(name = "REVISION")
    private String revision;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifecyclePhase;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Integer workflowDefId;

    @Transient
    private SpecPermission specPermission;

    @Transient
    private List<PLMSubscribe> subscribes;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    public Specification() {
        super(PLMObjectType.SPECIFICATION);
    }


}
