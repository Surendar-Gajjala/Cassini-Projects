package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerAttribute;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface ManufacturerPartAttributeRepository extends JpaRepository<PLMManufacturerPartAttribute, Integer> {

    @Query("SELECT a FROM PLMManufacturerPartAttribute a WHERE a.id.objectId= :manufacturerPartId")
    List<PLMManufacturerPartAttribute> findByIdIn(@Param("manufacturerPartId") Integer manufacturerPartId);

    @Query("SELECT a FROM PLMManufacturerPartAttribute a WHERE a.id.attributeDef= :attributeId")
    List<PLMManufacturerPartAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMManufacturerPartAttribute a WHERE a.id.objectId= :manufacturerPartId AND a.id.attributeDef= :attributeId"
    )
    PLMManufacturerPartAttribute findByMfrPartAndAttribute(@Param("manufacturerPartId") Integer manufacturerPartId, @Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMManufacturerPartAttribute a WHERE a.id.objectId= :manufacturerPartId  ORDER BY a.id.attributeDef ASC"
    )
    List<PLMManufacturerPartAttribute> getByManufacturerPartId(@Param("manufacturerPartId") Integer manufacturerPartId);
}
