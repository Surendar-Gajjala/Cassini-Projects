package com.cassinisys.plm.model.pqm;

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
@Table(name = "PQM_PRODUCT_INSPECTION_PLAN")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMProductInspectionPlan extends PQMInspectionPlan {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLAN_TYPE")
    private PQMProductInspectionPlanType planType;

    @Column(name = "PRODUCT")
    private Integer product;

    public PQMProductInspectionPlan() {
        super.setObjectType(PLMObjectType.PRODUCTINSPECTIONPLAN);
    }


}
