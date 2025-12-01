package com.cassinisys.is.repo.procm;
/**
 * The Class is for ItemAttributeRepository
 **/

import com.cassinisys.is.model.procm.ISManpowerItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManpowerItemAttributeRepository extends JpaRepository<ISManpowerItemAttribute, Integer> {

    @Query(
            "SELECT a FROM ISManpowerItemAttribute a WHERE a.id.objectId= :itemId"
    )
    /**
     * The method used to findByItemId of ISMaterialItemAttribute
     **/
    List<ISManpowerItemAttribute> findByItemId(@Param("itemId") Integer itemId);

    @Query(
            "SELECT a FROM ISManpowerItemAttribute a WHERE a.id.attributeDef= :attributeId"
    )
    List<ISManpowerItemAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);

}
