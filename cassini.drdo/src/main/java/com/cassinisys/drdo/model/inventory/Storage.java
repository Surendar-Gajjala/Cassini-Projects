package com.cassinisys.drdo.model.inventory;

import com.cassinisys.drdo.model.bom.Bom;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 07-10-2018.
 */
@Entity
@Table(name = "STORAGE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class Storage extends CassiniObject {

    @Transient
    List<Storage> children = new ArrayList<>();
    @ApiObjectField
    @Column(name = "NAME")
    private String name;
    @ApiObjectField
    @Column(name = "DESCRIPTION")
    private String description;
    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.inventory.StorageType")})
    @Column(name = "TYPE", nullable = false)
    private StorageType type;
    @ApiObjectField
    @Column(name = "PARENT")
    private Integer parent;
    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOM")
    private Bom bom;
    @ApiObjectField
    @Column(name = "IS_LEAFNODE")
    private Boolean isLeafNode = Boolean.FALSE;
    @ApiObjectField
    @Column(name = "CAPACITY")
    private Double capacity = 0.0;
    @ApiObjectField
    @Column(name = "REMAINING_CAPACITY")
    private Double remainingCapacity = 0.0;
    @ApiObjectField
    @Column(name = "ON_HOLD")
    private Boolean onHold = Boolean.FALSE;
    @ApiObjectField
    @Column(name = "RETURNED")
    private Boolean returned = Boolean.FALSE;

    @Transient
    private String storagePath;

    public Storage() {
        super(ObjectType.STORAGE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StorageType getType() {
        return type;
    }

    public void setType(StorageType type) {
        this.type = type;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Bom getBom() {
        return bom;
    }

    public void setBom(Bom bom) {
        this.bom = bom;
    }

    public Boolean getOnHold() {
        return onHold;
    }

    public void setOnHold(Boolean onHold) {
        this.onHold = onHold;
    }

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }

    public List<Storage> getChildren() {
        return children;
    }

    public void setChildren(List<Storage> children) {
        this.children = children;
    }

    public Boolean getIsLeafNode() {
        return isLeafNode;
    }

    public void setIsLeafNode(Boolean isLeafNode) {
        this.isLeafNode = isLeafNode;
    }

    public Double getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(Double remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    public Double getCapacity() {

        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
}
