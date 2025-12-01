package com.cassinisys.plm.model.mes;


import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 19-09-2020.
 */
@Entity
@Table(name = "MES_PRODUCTION_ORDER_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=true)
public class MESProductionOrderType extends MESObjectType {


    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    public MESProductionOrderType() {
        super.setObjectType(MESType.PRODUCTIONORDERTYPE);
    }

    @Transient
    private List<MESProductionOrderType> childrens = new ArrayList<>();

}
