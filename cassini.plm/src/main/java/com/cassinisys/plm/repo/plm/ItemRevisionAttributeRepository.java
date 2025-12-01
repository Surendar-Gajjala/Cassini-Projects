package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMItemRevisionAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 002 2-May -17.
 */
@Repository
public interface ItemRevisionAttributeRepository extends JpaRepository<PLMItemRevisionAttribute, Integer> {

    @Query(
            "SELECT a FROM PLMItemRevisionAttribute a WHERE a.id.objectId= :itemId"
    )
    List<PLMItemRevisionAttribute> findByItemId(@Param("itemId") Integer itemId);

    @Query(
            "SELECT a FROM PLMItemRevisionAttribute a WHERE a.id.objectId= :itemRevisionId AND a.id.attributeDef= :attributeId"
    )
    PLMItemRevisionAttribute findByItemRevisionAndAttribute(@Param("itemRevisionId") Integer itemRevisionId, @Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMItemRevisionAttribute a WHERE a.id.objectId= :itemId  ORDER BY a.id.attributeDef ASC"
    )
    List<PLMItemRevisionAttribute> getByItemId(@Param("itemId") Integer itemId);
}
