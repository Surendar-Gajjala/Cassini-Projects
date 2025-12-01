package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMInspectionPlanAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 13-06-2020.
 */
@Repository
public interface InspectionPlanAttributeRepository extends JpaRepository<PQMInspectionPlanAttribute, Integer> {
    @Query(
            "SELECT a FROM PQMInspectionPlanAttribute a WHERE a.id.objectId= :planId"
    )
    List<PQMInspectionPlanAttribute> findByInspectionPlanIdIn(@Param("planId") Integer planId);

    @Query(
            "SELECT a FROM PQMInspectionPlanAttribute a WHERE a.id.objectId= :itemId AND a.id.attributeDef= :attributeId"
    )
    PQMInspectionPlanAttribute findByInspectionAndAttribute(@Param("itemId") Integer itemId, @Param("attributeId") Integer attributeId);
}
