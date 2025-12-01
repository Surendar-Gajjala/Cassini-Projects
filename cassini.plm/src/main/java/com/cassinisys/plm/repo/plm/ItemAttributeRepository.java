package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemAttributeRepository extends JpaRepository<PLMItemAttribute, Integer> {

    @Query(
            "SELECT a FROM PLMItemAttribute a WHERE a.id.objectId= :itemId"
    )
    List<PLMItemAttribute> findByItemId(@Param("itemId") Integer itemId);

    @Query(
            "SELECT a FROM PLMItemAttribute a WHERE a.id.attributeDef= :attributeId"
    )
    List<PLMItemAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMItemAttribute a WHERE a.id.objectId= :itemId AND a.id.attributeDef= :attributeId"
    )
    PLMItemAttribute findByItemAndAttribute(@Param("itemId") Integer itemId, @Param("attributeId") Integer attributeId);


    @Query(
            "SELECT a FROM PLMItemAttribute a WHERE a.id.objectId= :itemId  ORDER BY a.id.attributeDef ASC"
    )
    List<PLMItemAttribute> getByItemId(@Param("itemId") Integer itemId);

}
