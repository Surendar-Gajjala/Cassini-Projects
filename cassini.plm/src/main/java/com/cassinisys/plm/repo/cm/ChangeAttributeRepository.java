package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMChangeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 1/3/2016.
 */
@Repository
public interface ChangeAttributeRepository extends JpaRepository<PLMChangeAttribute, Integer> {

    @Query(
            "SELECT a FROM PLMChangeAttribute a WHERE a.id.objectId= :changeId"
    )
    List<PLMChangeAttribute> findByChangeIdIn(@Param("changeId") Integer changeId);

    @Query(
            "SELECT a FROM PLMChangeAttribute a WHERE a.id.attributeDef= :attributeId"
    )
    List<PLMChangeAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMChangeAttribute a WHERE a.id.objectId= :changeId AND a.id.attributeDef= :attributeId"
    )
    PLMChangeAttribute findByChangeAndAttribute(@Param("changeId") Integer itemId, @Param("attributeId") Integer attributeId);
}
