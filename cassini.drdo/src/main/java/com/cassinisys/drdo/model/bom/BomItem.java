package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 07-10-2018.
 */
@Entity
@Table(name = "BOMITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class BomItem extends CassiniObject {

    @ApiObjectField
    @Column(name = "BOM")
    private Integer bom;

    @ApiObjectField
    @Column(name = "PARENT")
    private Integer parent;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM", nullable = false)
    private ItemRevision item;

    @ApiObjectField
    @Column(name = "QUANTITY")
    private Integer quantity = 0;

    @ApiObjectField
    @Column(name = "FRACTIONAL_QUANTITY")
    private Double fractionalQuantity = 0.0;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.bom.BomItemType")})
    @Column(name = "BOMITEM_TYPE")
    private BomItemType bomItemType = BomItemType.SECTION;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPEREF")
    private BomGroup typeRef;

    @ApiObjectField
    @Column(name = "HIERARCHICAL_CODE")
    private String hierarchicalCode;

    @ApiObjectField
    @Column(name = "UNIQUE_CODE")
    private String uniqueCode;

    @ApiObjectField
    @Column(name = "WORK_CENTER")
    private String workCenter;

    @Transient
    private String namePath = "";

    @Transient
    private String uniquePath = "";

    @Transient
    private String idPath = "";

    @Transient
    private Item newItem;

    @Transient
    private Integer pathCount = 0;

    @Transient
    private List<BomGroup> sections = new ArrayList<>();

    @Transient
    private BomGroup defaultSection = null;

    @Transient
    private List<Storage> storages = new ArrayList<>();

    @Transient
    private List<BomItem> children = new ArrayList<>();

    @Transient
    private List<BomItem> bomChildren = new ArrayList<>();

    @Transient
    private Integer level = 0;

    @Transient
    private Integer inwardQty = 0;

    @Transient
    private Double fractionalInwardQty = 0.0;

    @Transient
    private Boolean expanded = Boolean.FALSE;

    public BomItem() {
        super(DRDOObjectType.BOMITEM);
    }

    public Integer getBom() {
        return bom;
    }

    public void setBom(Integer bom) {
        this.bom = bom;
    }

    public ItemRevision getItem() {
        return item;
    }

    public void setItem(ItemRevision item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Item getNewItem() {
        return newItem;
    }

    public void setNewItem(Item newItem) {
        this.newItem = newItem;
    }

    public Double getFractionalQuantity() {
        return fractionalQuantity;
    }

    public void setFractionalQuantity(Double fractionalQuantity) {
        this.fractionalQuantity = fractionalQuantity;
    }

    public String getNamePath() {
        return namePath;
    }

    public void setNamePath(String namePath) {
        this.namePath = namePath;
    }

    public Integer getPathCount() {
        return pathCount;
    }

    public void setPathCount(Integer pathCount) {
        this.pathCount = pathCount;
    }

    public String getIdPath() {
        return idPath;
    }

    public void setIdPath(String idPath) {
        this.idPath = idPath;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public BomItemType getBomItemType() {
        return bomItemType;
    }

    public void setBomItemType(BomItemType bomItemType) {
        this.bomItemType = bomItemType;
    }

    public BomGroup getTypeRef() {
        return typeRef;
    }

    public void setTypeRef(BomGroup typeRef) {
        this.typeRef = typeRef;
    }

    public List<BomItem> getChildren() {
        return children;
    }

    public void setChildren(List<BomItem> children) {
        this.children = children;
    }

    public String getHierarchicalCode() {
        return hierarchicalCode;
    }

    public void setHierarchicalCode(String hierarchicalCode) {
        this.hierarchicalCode = hierarchicalCode;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public List<BomGroup> getSections() {
        return sections;
    }

    public void setSections(List<BomGroup> sections) {
        this.sections = sections;
    }

    public BomGroup getDefaultSection() {
        return defaultSection;
    }

    public void setDefaultSection(BomGroup defaultSection) {
        this.defaultSection = defaultSection;
    }

    public List<Storage> getStorages() {
        return storages;
    }

    public void setStorages(List<Storage> storages) {
        this.storages = storages;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getUniquePath() {
        return uniquePath;
    }

    public void setUniquePath(String uniquePath) {
        this.uniquePath = uniquePath;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public String getWorkCenter() {
        return workCenter;
    }

    public void setWorkCenter(String workCenter) {
        this.workCenter = workCenter;
    }

    public Integer getInwardQty() {
        return inwardQty;
    }

    public void setInwardQty(Integer inwardQty) {
        this.inwardQty = inwardQty;
    }

    public Double getFractionalInwardQty() {
        return fractionalInwardQty;
    }

    public void setFractionalInwardQty(Double fractionalInwardQty) {
        this.fractionalInwardQty = fractionalInwardQty;
    }

    public List<BomItem> getBomChildren() {
        return bomChildren;
    }

    public void setBomChildren(List<BomItem> bomChildren) {
        this.bomChildren = bomChildren;
    }
}
