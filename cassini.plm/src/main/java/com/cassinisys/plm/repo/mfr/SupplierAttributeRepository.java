package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerPartAttribute;
import com.cassinisys.plm.model.mfr.PLMSupplierAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Repository
public interface SupplierAttributeRepository extends JpaRepository<PLMSupplierAttribute, Integer> {

    @Query("SELECT a FROM PLMSupplierAttribute a WHERE a.id.objectId= :supplierId")
    List<PLMSupplierAttribute> findByIdIn(@Param("supplierId") Integer supplierId);

    @Query("SELECT a FROM PLMSupplierAttribute a WHERE a.id.attributeDef= :attributeId")
    List<PLMSupplierAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMSupplierAttribute a WHERE a.id.objectId= :supplierId AND a.id.attributeDef= :attributeId"
    )
    PLMSupplierAttribute findBySupplierAndAttribute(@Param("supplierId") Integer supplierId, @Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMSupplierAttribute a WHERE a.id.objectId= :supplierId  ORDER BY a.id.attributeDef ASC"
    )
    List<PLMSupplierAttribute> getBySupplierId(@Param("supplierId") Integer supplierId);

}
