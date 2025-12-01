package com.cassinisys.plm.model.mes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "MES_MBOM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESMBOM extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESMBOMType type;

    @Column(name = "ITEM")
    private Integer item;

    @Column(name = "LATEST_REVISION")
    private Integer latestRevision;

    @Column(name = "LATEST_RELEASED_REVISION")
    private Integer latestReleasedRevision;

    @Transient
    private Integer workflowDefId;
    @Transient
    private Integer itemRevision;
    @Transient
    private String itemName;
    @Transient
    private String revision;
    @Transient
    private Boolean pendingMco = false;

    public MESMBOM() {
        super.setObjectType(MESEnumObject.MBOM);
    }

}
