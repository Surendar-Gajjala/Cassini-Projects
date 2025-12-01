package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMSupplierPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 2-11-2020.
 */
@Repository
public interface SupplierPartRepository extends JpaRepository<PLMSupplierPart, Integer>, QueryDslPredicateExecutor<PLMSupplierPart> {
    List<PLMSupplierPart> findBySupplierOrderByModifiedDateDesc(Integer supplierId);

    @Query("select i.manufacturerPart.id from PLMSupplierPart i where i.supplier= :id and i.manufacturerPart.id not in :ids")
    List<Integer> getPartIdBySupplier(@Param("id") Integer id, @Param("ids") Iterable<Integer> ids);

    @Query("select i.manufacturerPart.id from PLMSupplierPart i where i.supplier= :id")
    List<Integer> getSupplierParts(@Param("id") Integer id);

    @Query("select count (i) from PLMSupplierPart i where i.manufacturerPart.id= :partId")
    Integer getMfrPartCount(@Param("partId") Integer partId);

    PLMSupplierPart findBySupplierAndManufacturerPart(Integer supplierId, PLMManufacturerPart manufacturerPart);
}