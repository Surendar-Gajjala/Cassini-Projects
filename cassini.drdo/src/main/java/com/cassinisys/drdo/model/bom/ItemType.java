package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Lov;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 03-10-2018.
 */
@Entity
@Table(name = "ITEMTYPE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class ItemType extends CassiniObject {

    @Transient
    List<ItemType> children = new ArrayList<>();
    @ApiObjectField
    @Column(name = "NAME")
    private String name;
    @ApiObjectField
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "TYPE_CODE")
    private String typeCode;
    @ApiObjectField(required = true)
    @Column(name = "PARENT_TYPE")
    private Integer parentType;
    @ApiObjectField
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ITEMNUMBER_SOURCE", nullable = true)
    private AutoNumber itemNumberSource;
    @ApiObjectField
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "REVISION_SEQUENCE", nullable = true)
    private Lov revisionSequence;
    @ApiObjectField
    @Column(name = "UNITS")
    private String units;
    @ApiObjectField
    @Column(name = "HAS_BOM")
    private Boolean hasBom = Boolean.FALSE;
    @ApiObjectField
    @Column(name = "HAS_LOTS")
    private Boolean hasLots = Boolean.FALSE;
    @ApiObjectField
    @Column(name = "HAS_SPEC")
    private Boolean hasSpec = Boolean.FALSE;
    @ApiObjectField
    @Column(name = "PARENT_NODE")
    private Boolean parentNode = Boolean.FALSE;
    @ApiObjectField
    @Column(name = "SEQUENCE_NUMBER")
    private String sequenceNumber = "00";
    @Transient
    private String parentNodeItemType;

    public ItemType() {
        super(DRDOObjectType.ITEMTYPE);
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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getParentType() {
        return parentType;
    }

    public void setParentType(Integer parentType) {
        this.parentType = parentType;
    }

    public AutoNumber getItemNumberSource() {
        return itemNumberSource;
    }

    public void setItemNumberSource(AutoNumber itemNumberSource) {
        this.itemNumberSource = itemNumberSource;
    }

    public Lov getRevisionSequence() {
        return revisionSequence;
    }

    public void setRevisionSequence(Lov revisionSequence) {
        this.revisionSequence = revisionSequence;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Boolean getHasLots() {
        return hasLots;
    }

    public void setHasLots(Boolean hasLots) {
        this.hasLots = hasLots;
    }

    public List<ItemType> getChildren() {
        return children;
    }

    public void setChildren(List<ItemType> children) {
        this.children = children;
    }

    public Boolean getParentNode() {
        return parentNode;
    }

    public void setParentNode(Boolean parentNode) {
        this.parentNode = parentNode;
    }

    public Boolean getHasSpec() {
        return hasSpec;
    }

    public void setHasSpec(Boolean hasSpec) {
        this.hasSpec = hasSpec;
    }

    public Boolean getHasBom() {
        return hasBom;
    }

    public void setHasBom(Boolean hasBom) {
        this.hasBom = hasBom;
    }

    public String getParentNodeItemType() {
        return parentNodeItemType;
    }

    public void setParentNodeItemType(String parentNodeItemType) {
        this.parentNodeItemType = parentNodeItemType;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
