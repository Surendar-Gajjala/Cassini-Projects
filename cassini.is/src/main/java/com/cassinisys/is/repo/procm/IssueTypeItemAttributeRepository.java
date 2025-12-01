package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISIssueTypeItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 20/06/19.
 */
@Repository
public interface IssueTypeItemAttributeRepository extends JpaRepository<ISIssueTypeItemAttribute, Integer> {

    @Query(
            "SELECT a FROM ISIssueTypeItemAttribute a WHERE a.id.objectId= :itemId"
    )
    /**
     * The method used to findByItemId of ISMaterialItemAttribute
     **/
    List<ISIssueTypeItemAttribute> findByItemId(@Param("itemId") Integer itemId);

    @Query(
            "SELECT a FROM ISIssueTypeItemAttribute a WHERE a.id.attributeDef= :attributeId"
    )
    List<ISIssueTypeItemAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);
}
