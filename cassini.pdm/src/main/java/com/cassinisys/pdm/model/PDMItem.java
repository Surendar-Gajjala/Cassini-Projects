package com.cassinisys.pdm.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Entity
@Table(name = "PDM_ITEM")
@PrimaryKeyJoinColumn(name = "ITEM_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMItem extends CassiniObject{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM_TYPE")
    @ApiObjectField(required = true)
    private PDMItemType itemType;

    @ApiObjectField(required = true)
    @Column(name = "ITEM_NUMBER")
    private String itemNumber;

    @ApiObjectField
    @Column(name = "DESCRIPTION")
    private String description;

    @ApiObjectField
    @Column(name = "REVISION")
    private String revision;

    @ApiObjectField
    @Column(name = "STATUS")
    private String status;

    @ApiObjectField(required = true)
    @Column(name = "ACTIVE")
    private boolean active;

    @ApiObjectField
    @Column(name = "UNITS")
    private String units = "Each";

    @ApiObjectField
    @Column(name = "UNIT_PRICE")
    private double unitPrice;

    @ApiObjectField
    @Column(name = "UNIT_COST")
    private double unitCost;

    @ApiObjectField
    @Column(name = "PICTURE")
    private byte[] picture;

    @ApiObjectField
    @Column(name = "HAS_BOM")
    private boolean hasBom = Boolean.FALSE;

    @ApiObjectField
    @Column(name = "HAS_FILES")
    private boolean hasFiles = Boolean.FALSE;

    @Transient
    List<PDMItem> children = new ArrayList<PDMItem>();

    public PDMItem(){super(PDMObjectType.ITEM);}

    public PDMItemType getItemType() {
        return itemType;
    }

    public void setItemType(PDMItemType itemType) {
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

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public boolean isHasBom() {
        return hasBom;
    }

    public void setHasBom(boolean hasBom) {
        this.hasBom = hasBom;
    }

    public boolean isHasFiles() {
        return hasFiles;
    }

    public void setHasFiles(boolean hasFiles) {
        this.hasFiles = hasFiles;
    }

    public List<PDMItem> getChildren() {
        return children;
    }

    public void setChildren(List<PDMItem> children) {
        this.children = children;
    }
}
