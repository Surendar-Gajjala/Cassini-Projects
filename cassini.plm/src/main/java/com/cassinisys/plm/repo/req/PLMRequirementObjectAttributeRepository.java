package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementObjectAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementObjectAttributeRepository extends JpaRepository<PLMRequirementObjectAttribute, Integer> {
    @Query(
            "SELECT a FROM PLMRequirementObjectAttribute a WHERE a.id.objectId= :objectId AND a.id.attributeDef= :attributeId"
    )
    PLMRequirementObjectAttribute findByObjectAndAttribute(@Param("objectId") Integer itemId, @Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMRequirementObjectAttribute a WHERE a.id.objectId= :objectId"
    )
    List<PLMRequirementObjectAttribute> findByObjectId(@Param("objectId") Integer objectId);

    @Query(
            "SELECT a FROM PLMRequirementObjectAttribute a WHERE a.id.attributeDef= :attributeId"
    )
    List<PLMRequirementObjectAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);
}
