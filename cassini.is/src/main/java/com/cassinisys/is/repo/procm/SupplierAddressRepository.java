package com.cassinisys.is.repo.procm;
/**
 * The Class is for SupplierAddressRepository
 **/

import com.cassinisys.is.model.procm.ISSupplierAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupplierAddressRepository extends
        JpaRepository<ISSupplierAddress, Integer> {

    @Query("select sa.id.addressId from ISSupplierAddress sa where sa.id.supplierId = :supplierId")
    /**
     * The method used to findBySupplierId of paged Integer
     **/
    public Page<Integer> findBySupplierId(
            @Param(value = "supplierId") Integer supplierId, Pageable pageable);

}
