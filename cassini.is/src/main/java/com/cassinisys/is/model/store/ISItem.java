package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Entity
@Table(name = "IS_ITEM")
@PrimaryKeyJoinColumn(name = "ITEM_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class ISItem extends CassiniObject {

    @Column(name = "ITEMNAME", nullable = false)
    @ApiObjectField(required = true)
    private String itemName;

    @Column(name = "ITEM_NUMBER", nullable = false)
    @ApiObjectField(required = true)
    private String itemNumber;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    @Column(name = "UNITS", nullable = false)
    @ApiObjectField(required = true)
    private String units;

    @Column(name = "UNIT_PRICE", nullable = false)
    @ApiObjectField(required = true)
    private Double unitPrice;

    @Column(name = "UNIT_COST", nullable = false)
    @ApiObjectField(required = true)
    private Double unitCost;

    @Column(name = "ACTUALCOST", nullable = false)
    @ApiObjectField(required = true)
    private Double actualCost;

    @Column(name = "QUANTITY", nullable = false)
    @ApiObjectField(required = true)
    private Double quantity;

    @Column(name = "ISSAVEDITEM", nullable = false)
    @ApiObjectField(required = true)
    private Boolean isSavedItem;

    @Column(name = " RESTYPE", nullable = false)
    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.pm.ResourceType")})
    private ResourceType itemType;

    public ISItem() {
        super(ISObjectType.ITEM);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public Double getActualCost() {
        return actualCost;
    }

    public void setActualCost(Double actualCost) {
        this.actualCost = actualCost;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsSavedItem() {
        return isSavedItem;
    }

    public void setIsSavedItem(Boolean isSavedItem) {
        this.isSavedItem = isSavedItem;
    }

    public ResourceType getItemType() {
        return itemType;
    }

    public void setItemType(ResourceType itemType) {
        this.itemType = itemType;
    }
}
