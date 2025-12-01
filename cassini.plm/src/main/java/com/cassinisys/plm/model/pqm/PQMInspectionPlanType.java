package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_INSPECTION_PLAN_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMInspectionPlanType extends PQMQualityType {

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "INSPECTION_NUMBER_SOURCE", nullable = true)
    private AutoNumber inspectionNumberSource;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "REVISION_SEQUENCE", nullable = true)
    private Lov revisionSequence;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    public PQMInspectionPlanType() {
        super.setQualityType(QualityType.INSPECTIONPLANTYPE);
    }

}
