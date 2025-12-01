package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 09-10-2018.
 */
@Entity
@Table(name = "BOMINSTANCEITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class BomInstanceItem extends CassiniObject {

    @ApiObjectField
    @Column(name = "PARENT")
    private Integer parent;

    @ApiObjectField
    @Column(name = "BOM", nullable = false)
    private Integer bom;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM", nullable = false)
    private ItemRevision item;

    @ApiObjectField
    @Column(name = "BOMITEM")
    private Integer bomItem;

    @ApiObjectField
    @Column(name = "QUANTITY")
    private Integer quantity;

    @ApiObjectField
    @Column(name = "FRACTIONAL_QUANTITY")
    private Double fractionalQuantity;

    @Column(name = "HAS_PARTTRACKING")
    private Boolean hasPartTracking = Boolean.FALSE;

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
    @Column(name = "ID_PATH")
    private String idPath;

    @ApiObjectField
    @Column(name = "NAME_PATH")
    private String namePath;

    @ApiObjectField
    @Column(name = "PERCENTAGE")
    private Double percentage;

    @ApiObjectField
    @Column(name = "STATUS")
    private String status;

    @ApiObjectField
    @Column(name = "WORK_CENTER")
    private String workCenter;

    @Transient
    private Integer requestedQuantity = 0;

    @Transient
    private Double issuedQuantity = 0.0;

    @Transient
    private Double canRequestMore = 0.0;

    @Transient
    private String itemPath;

    @Transient
    private Double fractionalRequestedQuantity = 0.0;

    @Transient
    private Double failedQuantity = 0.0;

    @Transient
    private Double allocatedQty = 0.0;

    @Transient
    private Integer level = 0;

    @Transient
    private Boolean expandMode = Boolean.TRUE;

    @Transient
    private Boolean expanded = Boolean.FALSE;

    @Transient
    private List<BomInstanceItem> children = new ArrayList<>();

    @Transient
    private List<ItemInstance> issuedInstances = new ArrayList<>();

    @Transient
    private List<LotInstance> lotInstances = new ArrayList<>();

    @Transient
    private BomGroup section;

    public BomInstanceItem() {
        super(DRDOObjectType.BOMINSTANCEITEM);
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

    public Integer getBomItem() {
        return bomItem;
    }

    public void setBomItem(Integer bomItem) {
        this.bomItem = bomItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getFractionalQuantity() {
        return fractionalQuantity;
    }

    public void setFractionalQuantity(Double fractionalQuantity) {
        this.fractionalQuantity = fractionalQuantity;
    }

    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(Integer requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public Double getFractionalRequestedQuantity() {
        return fractionalRequestedQuantity;
    }

    public void setFractionalRequestedQuantity(Double fractionalRequestedQuantity) {
        this.fractionalRequestedQuantity = fractionalRequestedQuantity;
    }

    public String getNamePath() {
        return namePath;
    }

    public void setNamePath(String namePath) {
        this.namePath = namePath;
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

    public String getHierarchicalCode() {
        return hierarchicalCode;
    }

    public void setHierarchicalCode(String hierarchicalCode) {
        this.hierarchicalCode = hierarchicalCode;
    }

    public String getItemPath() {
        return itemPath;
    }

    public void setItemPath(String itemPath) {
        this.itemPath = itemPath;
    }

    public List<BomInstanceItem> getChildren() {
        return children;
    }

    public void setChildren(List<BomInstanceItem> children) {
        this.children = children;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public List<ItemInstance> getIssuedInstances() {
        return issuedInstances;
    }

    public void setIssuedInstances(List<ItemInstance> issuedInstances) {
        this.issuedInstances = issuedInstances;
    }

    public Boolean getHasPartTracking() {
        return hasPartTracking;
    }

    public void setHasPartTracking(Boolean hasPartTracking) {
        this.hasPartTracking = hasPartTracking;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getIssuedQuantity() {
        return issuedQuantity;
    }

    public void setIssuedQuantity(Double issuedQuantity) {
        this.issuedQuantity = issuedQuantity;
    }

    public Double getFailedQuantity() {
        return failedQuantity;
    }

    public void setFailedQuantity(Double failedQuantity) {
        this.failedQuantity = failedQuantity;
    }

    public Double getCanRequestMore() {
        return canRequestMore;
    }

    public void setCanRequestMore(Double canRequestMore) {
        this.canRequestMore = canRequestMore;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getExpandMode() {
        return expandMode;
    }

    public void setExpandMode(Boolean expandMode) {
        this.expandMode = expandMode;
    }

    public Double getAllocatedQty() {
        return allocatedQty;
    }

    public void setAllocatedQty(Double allocatedQty) {
        this.allocatedQty = allocatedQty;
    }

    public List<LotInstance> getLotInstances() {
        return lotInstances;
    }

    public void setLotInstances(List<LotInstance> lotInstances) {
        this.lotInstances = lotInstances;
    }

    public BomGroup getSection() {
        return section;
    }

    public void setSection(BomGroup section) {
        this.section = section;
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
}

