package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MES_BOP_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESBOPType extends MESObjectType {

    @Transient
    private List<MESBOPType> childrens = new ArrayList<>();

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "REVISION_SEQUENCE", nullable = true)
    private Lov revisionSequence;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    public MESBOPType() {
        super.setObjectType(MESType.BOPTYPE);
    }
}