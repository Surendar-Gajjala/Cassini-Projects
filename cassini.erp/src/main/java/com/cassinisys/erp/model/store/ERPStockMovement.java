package com.cassinisys.erp.model.store;

import com.cassinisys.erp.converters.CustomDateDeserializer;
import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrder;
import com.cassinisys.erp.model.production.InventoryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ERP_STOCKMOVEMENT")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class ERPStockMovement implements Serializable {

    @ApiObjectField(required = true)
    @Id
    @SequenceGenerator(name = "TOPSTOCKMOVEMENT_ID_GEN", sequenceName = "TOPSTOCKMOVEMENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOPSTOCKMOVEMENT_ID_GEN")
    @Column(name = "ROWID")
    private Integer rowId;

    @Column(name = "ITEM", nullable = false)
    @ApiObjectField(required = true)
    private Integer item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORE")
    @ApiObjectField(required = true)
    private ERPStore store;

    @ApiObjectField(required = true)
    private ERPMaterialPurchaseOrder materialPO;


    @Column(name = "MOVEMENT", nullable = false)
    @Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.InventoryType")})
    @ApiObjectField(required = true)
    private InventoryType movementType;

    @Column(name = "QUANTITY", nullable = false)
    @ApiObjectField(required = true)
    private Integer quantity;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP", nullable = false)
    @ApiObjectField(required = true)
    private Date timeStamp;

    @Column(name = "RECORDED_BY", nullable = false)
    @ApiObjectField(required = true)
    private Integer recordedBy;

    @Column
    private String itemNumber;

    @Column
    private Integer project;

    @Transient
    private String reference;

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public ERPStore getStore() {
        return store;
    }

    public void setStore(ERPStore store) {
        this.store = store;
    }

    public InventoryType getMovementType() {
        return movementType;
    }

    public void setMovementType(InventoryType movementType) {
        this.movementType = movementType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(Integer recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ERPMaterialPurchaseOrder getMaterialPO() {
        return materialPO;
    }

    public void setMaterialPO(ERPMaterialPurchaseOrder materialPO) {
        this.materialPO = materialPO;
    }
}
