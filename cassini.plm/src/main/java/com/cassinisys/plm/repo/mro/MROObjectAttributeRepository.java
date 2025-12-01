package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROObjectAttributeRepository extends JpaRepository<MROObjectAttribute, Integer> {

    @Query(
            "SELECT a FROM MROObjectAttribute a WHERE a.id.objectId= :objectId AND a.id.attributeDef= :attributeId"
    )
    MROObjectAttribute findByObjectAndAttribute(@Param("objectId") Integer itemId, @Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM MROObjectAttribute a WHERE a.id.objectId= :objectId"
    )
    List<MROObjectAttribute> findByObjectId(@Param("objectId") Integer objectId);

    @Query("SELECT a FROM MROObjectAttribute a WHERE a.id.attributeDef= :attributeDef")
    List<MROObjectAttribute> findByAttributeDef(@Param("attributeDef") Integer var1);

}
