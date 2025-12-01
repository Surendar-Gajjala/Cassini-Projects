package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCObjectAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface PGCObjectAttributeRepository extends JpaRepository<PGCObjectAttribute, Integer> {
    @Query(
            "SELECT a FROM PGCObjectAttribute a WHERE a.id.objectId= :objectId AND a.id.attributeDef= :attributeId"
    )
    PGCObjectAttribute findByObjectAndAttribute(@Param("objectId") Integer itemId, @Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PGCObjectAttribute a WHERE a.id.objectId= :objectId"
    )
    List<PGCObjectAttribute> findByObjectId(@Param("objectId") Integer objectId);

    @Query("SELECT a FROM PGCObjectAttribute a WHERE a.id.attributeDef= :attributeDef")
    List<PGCObjectAttribute> findByAttributeDef(@Param("attributeDef") Integer var1);

}
