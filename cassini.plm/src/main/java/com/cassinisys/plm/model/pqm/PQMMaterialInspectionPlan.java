package com.cassinisys.plm.model.pqm;

import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMObjectType;
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
@Table(name = "PQM_MATERIAL_INSPECTION_PLAN")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMMaterialInspectionPlan extends PQMInspectionPlan {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLAN_TYPE")
    private PQMMaterialInspectionPlanType planType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MATERIAL")
    private PLMManufacturerPart material;

    public PQMMaterialInspectionPlan() {
        super.setObjectType(PLMObjectType.MATERIALINSPECTIONPLAN);
    }


}

