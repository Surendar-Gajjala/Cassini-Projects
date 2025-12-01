package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESObjectAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface MESObjectAttributeRepository extends JpaRepository<MESObjectAttribute, Integer> {
    @Query(
            "SELECT a FROM MESObjectAttribute a WHERE a.id.objectId= :objectId AND a.id.attributeDef= :attributeId"
    )
    MESObjectAttribute findByObjectAndAttribute(@Param("objectId") Integer itemId, @Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM MESObjectAttribute a WHERE a.id.objectId= :objectId"
    )
    List<MESObjectAttribute> findByObjectId(@Param("objectId") Integer objectId);

    @Query("SELECT a FROM MESObjectAttribute a WHERE a.id.attributeDef= :attributeDef")
    List<MESObjectAttribute> findByAttributeDef(@Param("attributeDef") Integer var1);

}
