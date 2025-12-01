package com.cassinisys.is.model.procm;
/**
 * Model for ISMaterialItem
 */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Entity
@Table(name = "IS_MATERIALITEM")
@PrimaryKeyJoinColumn(name = "ITEM_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PROCM")
public class ISMaterialItem extends CassiniObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM_TYPE")
    @ApiObjectField(required = true)
    private ISMaterialType itemType;

    @Transient
    private Double scrapReqItemQuantity = 0.0;

    @Transient
    private String scrapReqItemDescription;

    @Column(name = "ITEM_NUMBER ", nullable = false)
    @ApiObjectField(required = true)
    private String itemNumber;
    @Column(name = "ITEM_NAME ", nullable = false)
    @ApiObjectField(required = true)
    private String itemName;
    @Column(name = "DESCRIPTION")
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
    public ISMaterialItem() {
        super(ISObjectType.MATERIAL);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ISMaterialType getItemType() {
        return itemType;
    }

    public void setItemType(ISMaterialType itemType) {
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Double getScrapReqItemQuantity() {
        return scrapReqItemQuantity;
    }

    public void setScrapReqItemQuantity(Double scrapReqItemQuantity) {
        this.scrapReqItemQuantity = scrapReqItemQuantity;
    }

    public String getScrapReqItemDescription() {
        return scrapReqItemDescription;
    }

    public void setScrapReqItemDescription(String scrapReqItemDescription) {
        this.scrapReqItemDescription = scrapReqItemDescription;
    }

}
