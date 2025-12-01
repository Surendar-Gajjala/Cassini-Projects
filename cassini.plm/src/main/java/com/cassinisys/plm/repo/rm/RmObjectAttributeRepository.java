package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RmObjectAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RmObjectAttributeRepository extends JpaRepository<RmObjectAttribute, Integer> {
    @Query(
            "SELECT a FROM RmObjectAttribute a WHERE a.id.objectId= :itemId"
    )
    List<RmObjectAttribute> findByObjectId(@Param("itemId") Integer itemId);

    @Query(
            "SELECT a FROM RmObjectAttribute a WHERE a.id.attributeDef= :attributeId"
    )
    List<RmObjectAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM RmObjectAttribute a WHERE a.id.objectId= :objectId AND a.id.attributeDef= :attributeId"
    )
    RmObjectAttribute findByObjectAndAttribute(@Param("objectId") Integer objectId, @Param("attributeId") Integer attributeId);

}