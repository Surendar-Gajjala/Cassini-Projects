package com.cassinisys.is.repo.procm;
/**
 * The Class is for ItemAttributeRepository
 **/

import com.cassinisys.is.model.procm.ISMachineItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineItemAttributeRepository extends JpaRepository<ISMachineItemAttribute, Integer> {

    @Query(
            "SELECT a FROM ISMachineItemAttribute a WHERE a.id.objectId= :itemId"
    )
    /**
     * The method used to findByItemId of ISMaterialItemAttribute
     **/
    List<ISMachineItemAttribute> findByItemId(@Param("itemId") Integer itemId);

    @Query(
            "SELECT a FROM ISMachineItemAttribute a WHERE a.id.attributeDef= :attributeId"
    )
    List<ISMachineItemAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);

}
