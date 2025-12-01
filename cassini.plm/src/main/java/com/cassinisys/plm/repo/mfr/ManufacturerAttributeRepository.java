package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerAttribute;
import com.cassinisys.plm.model.plm.PLMItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface ManufacturerAttributeRepository extends JpaRepository<PLMManufacturerAttribute, Integer> {

    @Query("SELECT a FROM PLMManufacturerAttribute a WHERE a.id.objectId= :manufacturerId")
    List<PLMManufacturerAttribute> findByIdIn(@Param("manufacturerId") Integer manufacturerId);

    @Query("SELECT a FROM PLMManufacturerAttribute a WHERE a.id.attributeDef= :attributeId")
    List<PLMManufacturerAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMManufacturerAttribute a WHERE a.id.objectId= :manufacturerId AND a.id.attributeDef= :attributeId"
    )
    PLMManufacturerAttribute findByManufacturerAndAttribute(@Param("manufacturerId") Integer manufacturerId, @Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMManufacturerAttribute a WHERE a.id.objectId= :manufacturerId  ORDER BY a.id.attributeDef ASC"
    )
    List<PLMManufacturerAttribute> getByManufacturerId(@Param("manufacturerId") Integer manufacturerId);
}
