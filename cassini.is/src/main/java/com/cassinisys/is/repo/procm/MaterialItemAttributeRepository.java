package com.cassinisys.is.repo.procm;
/**
 * The Class is for ItemAttributeRepository
 **/

import com.cassinisys.is.model.procm.ISMaterialItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialItemAttributeRepository extends JpaRepository<ISMaterialItemAttribute, Integer> {

    @Query(
            "SELECT a FROM ISMaterialItemAttribute a WHERE a.id.objectId= :itemId"
    )
    /**
     * The method used to findByItemId of ISMaterialItemAttribute
     **/
    List<ISMaterialItemAttribute> findByItemId(@Param("itemId") Integer itemId);

    @Query(
            "SELECT a FROM ISMaterialItemAttribute a WHERE a.id.attributeDef= :attributeId"
    )
    List<ISMaterialItemAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);

}
