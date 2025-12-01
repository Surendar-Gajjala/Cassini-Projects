package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by subra on 04-10-2018.
 */
@Entity
@Table(name = "ITEMREVISION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class ItemRevision extends CassiniObject {

    @ApiObjectField(required = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM_MASTER")
    private Item itemMaster;

    @ApiObjectField
    @Column(name = "REVISION")
    private String revision;

    @ApiObjectField
    @Column(name = "HAS_BOM")
    private Boolean hasBom;

    @ApiObjectField
    @Column(name = "HAS_FILES")
    private Boolean hasFiles;

    @ApiObjectField
    @Column(name = "DRAWING_NUMBER")
    private String drawingNumber;

    @ApiObjectField
    @Column(name = "DEFAULT_BOM")
    private Integer defaultBom;

    public ItemRevision() {
        super(DRDOObjectType.ITEMREVISION);
    }

    public Item getItemMaster() {
        return itemMaster;
    }

    public void setItemMaster(Item itemMaster) {
        this.itemMaster = itemMaster;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Boolean getHasBom() {
        return hasBom;
    }

    public void setHasBom(Boolean hasBom) {
        this.hasBom = hasBom;
    }

    public Boolean getHasFiles() {
        return hasFiles;
    }

    public void setHasFiles(Boolean hasFiles) {
        this.hasFiles = hasFiles;
    }

    public String getDrawingNumber() {
        return drawingNumber;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public Integer getDefaultBom() {
        return defaultBom;
    }

    public void setDefaultBom(Integer defaultBom) {
        this.defaultBom = defaultBom;
    }

    public ItemInstance createInstance(String instanceName) {
        ItemInstance instance = new ItemInstance();
        instance.setItem(this);
        instance.setInstanceName(instanceName);
        instance.setInitialUpn(instanceName);
        return instance;
    }
}
