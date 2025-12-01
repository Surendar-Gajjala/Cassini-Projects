package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by smukka on 04-10-2022.
 */
@Entity
@Table(name = "MES_PRODUCTION_ORDER_ITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESProductionOrderItem extends CassiniObject {

    @Column(name = "PRODUCTION_ORDER")
    private Integer productionOrder;

    @Column(name = "MBOM_REVISION")
    private Integer mbomRevision;

    @Column(name = "BOP_REVISION")
    private Integer bopRevision;

    @Column(name = "QUANTITY_PRODUCED")
    private Integer quantityProduced;

    public MESProductionOrderItem() {
        super.setObjectType(MESEnumObject.PRODUCTIONORDERITEM);
    }
}
