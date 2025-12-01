package com.cassinisys.is.model.procm;
/**
 * Model for ISMaterialItem
 */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Entity
@Table(name = "IS_MANPOWERITEM")
@PrimaryKeyJoinColumn(name = "ITEM_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PROCM")
public class ISManpowerItem extends CassiniObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM_TYPE")
    @ApiObjectField(required = true)
    private ISManpowerType itemType;

    @Column(name = "ITEM_NUMBER ", nullable = false)
    @ApiObjectField(required = true)
    private String itemNumber;

    @OneToOne
    @JoinColumn(name = "PERSON")
    @ApiObjectField(required = true)
    private Person person;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    @ApiObjectField(required = true)
    @Column(name = "ACTIVE")
    private boolean active;

    @ApiObjectField
    @Column(name = "UNITS")
    private String units = "Each";

    @Column(name = "UNIT_COST", nullable = false)
    @ApiObjectField(required = true)
    private double unitCost = 0.0;

    @Column(name = "UNIT_PRICE", nullable = false)
    @ApiObjectField(required = true)
    private double unitPrice = 0.0;

    @Column(name = "PICTURE")
    @ApiObjectField
    private byte[] picture;

    public ISManpowerItem() {
        super(ISObjectType.MANPOWER);
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ISManpowerType getItemType() {
        return itemType;
    }

    public void setItemType(ISManpowerType itemType) {
        this.itemType = itemType;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}