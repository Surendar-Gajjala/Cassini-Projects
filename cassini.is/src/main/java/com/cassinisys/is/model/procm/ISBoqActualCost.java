package com.cassinisys.is.model.procm;
/**
 * The class is for ISBoqActualCost
 */

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IS_BOQACTUALCOST")
@ApiObject(name = "PROCM")
public class ISBoqActualCost implements Serializable {

    @Id
    @SequenceGenerator(name = "BOQ_ACTUALCOST_ID_GEN", sequenceName = "BOQ_ACTUALCOST_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOQ_ACTUALCOST_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "BOQITEM", nullable = false)
    @ApiObjectField(required = true)
    private Integer boqItem;

    @Column(name = "COSTNAME", nullable = false)
    @ApiObjectField(required = true)
    private String costName;

    @Column(name = "COST", nullable = false)
    @ApiObjectField(required = true)
    private Double cost;

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBoqItem() {
        return boqItem;
    }

    public void setBoqItem(Integer boqItem) {
        this.boqItem = boqItem;
    }

    public String getCostName() {
        return costName;
    }

    public void setCostName(String costName) {
        this.costName = costName;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
