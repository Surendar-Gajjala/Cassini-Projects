package com.cassinisys.plm.model.mes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "MES_BOP")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESBOP extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESBOPType type;

    @Column(name = "LATEST_REVISION")
    private Integer latestRevision;

    @Column(name = "LATEST_RELEASED_REVISION")
    private Integer latestReleasedRevision;

    @Transient
    private Integer workflowDefinition;
    @Transient
    private Integer mbomRevision;

    @Transient
    private String printMbomRevision;

    @Transient
    private String createdPerson;

    @Transient
    private String modifiedPerson;

    public MESBOP() {
        super.setObjectType(MESEnumObject.BOP);
    }

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private Boolean finishWorkflow = Boolean.FALSE;
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";

}
