package com.cassinisys.is.model.procm;
/* Model for ISSupplierAddressId */

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ISSupplierAddressId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "SUPPLIER", nullable = false)
    @ApiObjectField(required = true)
    private Integer supplierId;

    @Column(name = "ADDRESS", nullable = false)
    @ApiObjectField(required = true)
    private Integer addressId;

    public ISSupplierAddressId() {
    }

    public ISSupplierAddressId(Integer supplierId, Integer addressId) {
        this.supplierId = supplierId;
        this.addressId = addressId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((addressId == null) ? 0 : addressId.hashCode());
        result = prime * result
                + ((supplierId == null) ? 0 : supplierId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISSupplierAddressId other = (ISSupplierAddressId) obj;
        if (addressId == null) {
            if (other.addressId != null)
                return false;
        } else if (!addressId.equals(other.addressId))
            return false;
        if (supplierId == null) {
            if (other.supplierId != null)
                return false;
        } else if (!supplierId.equals(other.supplierId))
            return false;
        return true;
    }

}
