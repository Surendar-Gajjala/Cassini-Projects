package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMInspectionPlanRevisionAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 13-06-2020.
 */
@Repository
public interface InspectionPlanRevisionAttributeRepository extends JpaRepository<PQMInspectionPlanRevisionAttribute, Integer> {
    @Query(
            "SELECT a FROM PQMInspectionPlanRevisionAttribute a WHERE a.id.objectId= :planId"
    )
    List<PQMInspectionPlanRevisionAttribute> findByInspectionPlanRevisionIdIn(@Param("planId") Integer planId);

    @Query(
            "SELECT a FROM PQMInspectionPlanRevisionAttribute a WHERE a.id.objectId= :itemId AND a.id.attributeDef= :attributeId"
    )
    PQMInspectionPlanRevisionAttribute findByInspectionPlanRevisionAndAttribute(@Param("itemId") Integer itemId, @Param("attributeId") Integer attributeId);
}
