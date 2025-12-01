package com.cassinisys.is.model.procm;
/**
 * Model for ISBoqItem
 */

import com.cassinisys.is.model.pm.ResourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IS_BOQITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PROCM")
public class ISBoqItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "BOQ_ITEM_ID_GEN",
            sequenceName = "BOQ_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "BOQ_ITEM_ID_GEN")
    @Column(name = "ITEM_ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "BOQ", nullable = false)
    @ApiObjectField(required = true)
    private Integer boq;

    @Column(name = "BOQNAME", nullable = false)
    @ApiObjectField(required = true)
    private String boqName;

    @Column(name = "ITEMNAME", nullable = false)
    @ApiObjectField(required = true)
    private String itemName;

    @Column(name = "ISSAVEDITEM", nullable = false)
    @ApiObjectField(required = true)
    private Boolean isSavedItem;

    @Column(name = "ACTUALCOST", nullable = false)
    @ApiObjectField(required = true)
    private Double actualCost;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    @Column(name = "ITEM_NUMBER", nullable = false)
    @ApiObjectField(required = true)
    private String itemNumber;

    @Column(name = "DESCRIPTION")
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

    @Column(name = "QUANTITY", nullable = false)
    @ApiObjectField(required = true)
    private Double quantity;

    @Column(name = " RESTYPE", nullable = false)
    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.pm.ResourceType")})
    private ResourceType itemType;

    @Column
    private String notes;

    @Transient
    private String resourceTypeName;

    @Transient
    private Double issuedQty;

    @Transient
    private Double inventory;

    @Transient
    private Double receivedQty;

    @Transient
    private Double totalBoqQuantity;

    @Transient
    private Double returnedQty;

    public ISBoqItem() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Boolean getIsSavedItem() {
        return isSavedItem;
    }

    public void setIsSavedItem(Boolean isSavedItem) {
        this.isSavedItem = isSavedItem;
    }

    public String getBoqName() {
        return boqName;
    }

    public void setBoqName(String boqName) {
        this.boqName = boqName;
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

    public ResourceType getItemType() {
        return itemType;
    }

    public void setItemType(ResourceType itemType) {
        this.itemType = itemType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBoq() {
        return boq;
    }

    public void setBoq(Integer boq) {
        this.boq = boq;
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

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Double issuedQty) {
        this.issuedQty = issuedQty;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public Double getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Double receivedQty) {
        this.receivedQty = receivedQty;
    }

    public Double getTotalBoqQuantity() {
        return totalBoqQuantity;
    }

    public void setTotalBoqQuantity(Double totalBoqQuantity) {
        this.totalBoqQuantity = totalBoqQuantity;
    }

    public Double getReturnedQty() {
        return returnedQty;
    }

    public void setReturnedQty(Double returnedQty) {
        this.returnedQty = returnedQty;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISBoqItem other = (ISBoqItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
