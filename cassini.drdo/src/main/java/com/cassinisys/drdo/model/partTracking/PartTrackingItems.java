package com.cassinisys.drdo.model.partTracking;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Entity
@Table(name = "PARTTRACKING_ITEMS")
public class PartTrackingItems implements Serializable{

    @ApiObjectField(required = true)
    @Id
    @SequenceGenerator(name = "PARTTRACKINGITEM_ID_GEN", sequenceName = "PARTTRACKINGITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTTRACKINGITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ITEM_ID")
    private Integer item;

    @Column(name = "PARTTRACKING_STEP")
    private Integer partTrackingStep;

    @Column(name = "SERIAL_NUMBER")
    private Integer serialNo;

    @Column(name = "STATUS")
    private String status;

    @Transient
    private String partTrackingStepValue;

    @Transient
    private String secItemNumber;

    @Transient
    private String section;

    @Transient
    private String missile;

    @Transient
    private String parentOfMissile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getPartTrackingStep() {
        return partTrackingStep;
    }

    public void setPartTrackingStep(Integer partTrackingStep) {
        this.partTrackingStep = partTrackingStep;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPartTrackingStepValue() {
        return partTrackingStepValue;
    }

    public void setPartTrackingStepValue(String partTrackingStepValue) {
        this.partTrackingStepValue = partTrackingStepValue;
    }

    public String getSecItemNumber() {
        return secItemNumber;
    }

    public void setSecItemNumber(String secItemNumber) {
        this.secItemNumber = secItemNumber;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getMissile() {
        return missile;
    }

    public void setMissile(String missile) {
        this.missile = missile;
    }

    public String getParentOfMissile() {
        return parentOfMissile;
    }

    public void setParentOfMissile(String parentOfMissile) {
        this.parentOfMissile = parentOfMissile;
    }
}
