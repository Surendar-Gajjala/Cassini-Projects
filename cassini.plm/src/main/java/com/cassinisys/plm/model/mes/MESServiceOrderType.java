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
@Table(name = "MES_SERVICE_ORDER_TYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESServiceOrderType extends MESObjectType {

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIFECYCLE", nullable = true)
    private PLMLifeCycle lifecycle;

    public MESServiceOrderType() {
        super.setObjectType(MESType.SERVICEORDERTYPE);
    }

    @Transient
    private List<MESServiceOrderType> childrens = new ArrayList<>();

}
