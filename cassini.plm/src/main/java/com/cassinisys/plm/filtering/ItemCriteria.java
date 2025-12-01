package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.plm.model.plm.ItemClass;

public class ItemCriteria extends Criteria {

    Integer itemType;
    String itemNumber;
    String itemName;
    String latestRevision;
    String status;
    String active;
    String unitPrice;
    String unitCost;
    String description;
    String searchQuery;
    ItemClass itemClass;
    Boolean latest = false;
    Integer itemId;
    Boolean bomCompare = false;
    Integer type;
    String phase;
    String revision;
    Integer relationShipRevision;
    Integer relationShip;
    String tagType;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public String getLatestRevision() {
        return latestRevision;
    }

    public void setLatestRevision(String latestRevision) {
        this.latestRevision = latestRevision;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(String unitCost) {
        this.unitCost = unitCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ItemClass getItemClass() {
        return itemClass;
    }

    public void setItemClass(ItemClass itemClass) {
        this.itemClass = itemClass;
    }

    public Boolean getLatest() {
        return latest;
    }

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Boolean getBomCompare() {
        return bomCompare;
    }

    public void setBomCompare(Boolean bomCompare) {
        this.bomCompare = bomCompare;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Integer getRelationShipRevision() {
        return relationShipRevision;
    }

    public void setRelationShipRevision(Integer relationShipRevision) {
        this.relationShipRevision = relationShipRevision;
    }

    public Integer getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(Integer relationShip) {
        this.relationShip = relationShip;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
}
