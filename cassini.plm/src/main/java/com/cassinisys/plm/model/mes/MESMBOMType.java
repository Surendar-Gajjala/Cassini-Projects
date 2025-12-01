package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Entity
@Table(name = "MES_MBOM_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESMBOMType extends MESObjectType {

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "REVISION_SEQUENCE", nullable = true)
    private Lov revisionSequence;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    @Transient
    private List<MESMBOMType> childrens = new ArrayList<>();

    public MESMBOMType() {
        super.setObjectType(MESType.MBOMTYPE);
    }
}