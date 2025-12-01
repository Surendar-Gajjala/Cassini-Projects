package com.cassinisys.plm.model.mes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Entity
@Table(name = "MES_SERVICE_ORDER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MESServiceOrder extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESServiceOrderType type;

    public MESServiceOrderType getType() {
        return type;
    }

    public void setType(MESServiceOrderType type) {
        this.type = type;
    }

    public MESServiceOrder() {
        super.setObjectType(MESEnumObject.SERVICEORDER);
    }
}
