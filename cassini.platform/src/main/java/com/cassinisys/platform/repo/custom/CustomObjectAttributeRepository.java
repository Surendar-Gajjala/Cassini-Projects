package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomObjectAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomObjectAttributeRepository extends JpaRepository<CustomObjectAttribute, Integer> {


    @Query(
            "SELECT a FROM CustomObjectAttribute a WHERE a.id.objectId= :customObjectId"
    )
    List<CustomObjectAttribute> findByIdIn(@Param("customObjectId") Integer customObjectId);

    @Query(
            "SELECT a FROM CustomObjectAttribute a WHERE a.id.objectId= :customObjectId AND a.id.attributeDef= :attributeId"
    )
    CustomObjectAttribute findByObjectAndAttribute(@Param("customObjectId") Integer customObjectId, @Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM CustomObjectAttribute a WHERE a.id.attributeDef= :attributeDefId"
    )
    List<CustomObjectAttribute> getAllObjectAttributesByAttributeDefId(@Param("attributeDefId") Integer attributeDefId);

    @Query(
            "SELECT a FROM CustomObjectAttribute a WHERE a.id.objectId= :objectId and a.id.attributeDef in :attributeDefIds"
    )
    List<CustomObjectAttribute> getObjectAttributesByObjectIdAndAttributeDefs(@Param("objectId") Integer objectId, @Param("attributeDefIds") List<Integer> attributeDefIds);
}
