package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMRelatedItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 023 23-Jan -18.
 */
@Repository
public interface RelatedItemAttributeRepository extends JpaRepository<PLMRelatedItemAttribute, Integer> {

    @Query(
            "SELECT a FROM PLMRelatedItemAttribute a WHERE a.id.objectId= :relatedItemId"
    )
    List<PLMRelatedItemAttribute> findByRelatedItemId(@Param("relatedItemId") Integer relatedItemId);
}
