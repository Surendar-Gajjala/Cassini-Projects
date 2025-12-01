package com.cassinisys.is.model.procm;
/* Model for ISSupplierAddress */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "IS_SUPPLIERADDRESS")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PROCM")
public class ISSupplierAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ISSupplierAddressId id;

    public ISSupplierAddress() {
    }

    public ISSupplierAddress(Integer supplierId, Integer addressId) {
        id = new ISSupplierAddressId(supplierId, addressId);
    }

    public ISSupplierAddress(ISSupplierAddressId id) {
        this.id = id;
    }

    public ISSupplierAddressId getId() {
        return id;
    }

    public void setId(ISSupplierAddressId id) {
        this.id = id;
    }

}
