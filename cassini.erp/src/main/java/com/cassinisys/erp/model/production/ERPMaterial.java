package com.cassinisys.erp.model.production;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by reddy on 7/18/15.
 */
@Entity
@Table(name = "ERP_MATERIAL")
@PrimaryKeyJoinColumn(name = "MATERIAL_ID")
@ApiObject(group = "PRODUCTION")
public class ERPMaterial extends ERPObject {

    @ApiObjectField(required = true)
    private String sku;
    @ApiObjectField(required = true)
    private ERPMaterialCategory category;
    @ApiObjectField(required = true)
    private ERPMaterialType type;
    @ApiObjectField(required = true)
    private String name;
    @ApiObjectField
    private String description;
    @ApiObjectField
    private byte[] picture;
    @ApiObjectField
    private String units = "Each";
    @ApiObjectField
    private Set<ERPSupplier> suppliers;
    @ApiObjectField
    private List<ERPMaterialSupplier> materialSuppliers;
    private ERPMaterialPurchaseOrder materialPO;

    public ERPMaterial() {
        super.setObjectType(ObjectType.MATERIAL);
    }

    @Column(name = "SKU")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY", nullable = false)
    public ERPMaterialCategory getCategory() {
        return category;
    }

    public void setCategory(ERPMaterialCategory category) {
        this.category = category;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE", nullable = false)
    public ERPMaterialType getType() {
        return type;
    }

    public void setType(ERPMaterialType type) {
        this.type = type;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "PICTURE")
    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @Column(name = "UNITS")
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Transient
    public Set<ERPSupplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<ERPSupplier> suppliers) {
        this.suppliers = suppliers;
    }

    @Transient
    public List<ERPMaterialSupplier> getMaterialSuppliers() {
        return materialSuppliers;
    }

    public void setMaterialSuppliers(List<ERPMaterialSupplier> materialSuppliers) {
        this.materialSuppliers = materialSuppliers;
    }

    @Transient
    public ERPMaterialPurchaseOrder getMaterialPO() {
        return materialPO;
    }

    public void setMaterialPO(ERPMaterialPurchaseOrder materialPO) {
        this.materialPO = materialPO;
    }
}
