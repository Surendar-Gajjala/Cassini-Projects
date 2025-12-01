package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subra on 04-10-2018.
 */
@Entity
@Table(name = "ITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class Item extends CassiniObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM_TYPE")
    @ApiObjectField(required = true)
    private ItemType itemType;

    @ApiObjectField(required = true)
    @Column(name = "ITEM_NUMBER")
    private String itemNumber;

    @ApiObjectField
    @Column(name = "ITEM_NAME")
    private String itemName;

    @ApiObjectField
    @Column(name = "DESCRIPTION")
    private String description;

    @ApiObjectField
    @Column(name = "ITEM_CODE")
    private String itemCode;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PART_SPEC")
    private ItemTypeSpecs partSpec;

    @ApiObjectField
    @Column(name = "THUMBNAIL")
    private byte[] thumbnail;

    @ApiObjectField
    @Column(name = "LATEST_REVISION")
    private Integer latestRevision;

    @ApiObjectField
    @Column(name = "LOCKED")
    private Boolean locked = Boolean.FALSE;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOCKED_BY")
    private Person lockedBy;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOCKED_DATE", nullable = false)
    @ApiObjectField(required = true)
    private Date lockedDate;

    @Column(name = "MATERIAL")
    private String material;


    @Transient
    private ItemType parentType;

    @Transient
    private String drawingNumber;

    @Transient
    private ItemTypeSpecs itemTypeSpec;

    @Transient
    private List<BomItem> bomItems = new ArrayList<>();

    @Transient
    private Boolean createdAsBomItem = false;

    public Item() {
        super(DRDOObjectType.ITEMMASTER);
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getLatestRevision() {
        return latestRevision;
    }

    public void setLatestRevision(Integer latestRevision) {
        this.latestRevision = latestRevision;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Person getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(Person lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Date getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(Date lockedDate) {
        this.lockedDate = lockedDate;
    }

    public String getDrawingNumber() {
        return drawingNumber;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public List<BomItem> getBomItems() {
        return bomItems;
    }

    public void setBomItems(List<BomItem> bomItems) {
        this.bomItems = bomItems;
    }

    public Boolean getCreatedAsBomItem() {
        return createdAsBomItem;
    }

    public void setCreatedAsBomItem(Boolean createdAsBomItem) {
        this.createdAsBomItem = createdAsBomItem;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public ItemTypeSpecs getItemTypeSpec() {
        return itemTypeSpec;
    }

    public void setItemTypeSpec(ItemTypeSpecs itemTypeSpec) {
        this.itemTypeSpec = itemTypeSpec;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public ItemTypeSpecs getPartSpec() {
        return partSpec;
    }

    public void setPartSpec(ItemTypeSpecs partSpec) {
        this.partSpec = partSpec;
    }

    public ItemType getParentType() {
        return parentType;
    }

    public void setParentType(ItemType parentType) {
        this.parentType = parentType;
    }
}
