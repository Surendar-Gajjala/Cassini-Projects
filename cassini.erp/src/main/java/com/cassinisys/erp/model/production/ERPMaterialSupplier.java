package com.cassinisys.erp.model.production;

import com.cassinisys.erp.model.hrm.ERPPayroll;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lakshmi on 2/17/2016.
 */

@Entity
@Table(name = "ERP_MATERIALSUPPLIER")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PROD")
public class ERPMaterialSupplier implements Serializable {

    @ApiObjectField(required = true)
    private MaterialSupplierId materialSupplierId;

    @EmbeddedId
    public MaterialSupplierId getMaterialSupplierId() {
        return materialSupplierId;
    }

    @ApiObjectField
    private Integer cost;

    public void setMaterialSupplierId(MaterialSupplierId materialSupplierId) {
        this.materialSupplierId = materialSupplierId;
    }

    @ApiObjectField(required = true)
    private String supplierName;

    @Embeddable
    public static class MaterialSupplierId implements Serializable {

        @ApiObjectField(required = true)
        private Integer materialId;

        @ApiObjectField(required = true)
        private Integer supplierId;

        public MaterialSupplierId() {

        }

        public MaterialSupplierId(Integer materialId, Integer supplierId) {
            super();
            this.materialId = materialId;
            this.supplierId = supplierId;
        }
        @Column(name = "MATERIAL_ID", nullable = false)
        public Integer getMaterialId() {
            return materialId;
        }

        public void setMaterialId(Integer materialId) {
            this.materialId = materialId;
        }

        @Column(name = "SUPPLIER_ID", nullable = false)
        public Integer getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(Integer supplierId) {
            this.supplierId = supplierId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MaterialSupplierId)) return false;

            MaterialSupplierId that = (MaterialSupplierId) o;

            if (!materialId.equals(that.materialId)) return false;
            return supplierId.equals(that.supplierId);

        }

        @Override
        public int hashCode() {
            int result = materialId.hashCode();
            result = 31 * result + supplierId.hashCode();
            return result;
        }
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @Transient
    public Integer getMaterialId() {
        return this.materialSupplierId.getMaterialId();
    }

    public void setMaterialId(Integer materialId) {
        this.materialSupplierId.setMaterialId(materialId);
    }

   @Transient
    public Integer getSupplierId() {
        return this.materialSupplierId.getSupplierId();
    }

    public void setSupplierId(Integer supplierId) {
        this.materialSupplierId.setSupplierId(supplierId);
    }

    @Transient
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public ERPMaterialSupplier() {
        this.materialSupplierId = new MaterialSupplierId();
    }
}