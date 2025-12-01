package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMProblemReportAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface ProblemReportAttributeRepository extends JpaRepository<PQMProblemReportAttribute, Integer> {
    @Query(
            "SELECT a FROM PQMProblemReportAttribute a WHERE a.id.objectId= :planId"
    )
    List<PQMProblemReportAttribute> findByProblemReportIdIn(@Param("planId") Integer planId);

    @Query(
            "SELECT a FROM PQMProblemReportAttribute a WHERE a.id.objectId= :itemId AND a.id.attributeDef= :attributeId"
    )
    PQMProblemReportAttribute findByProblemReportAndAttribute(@Param("itemId") Integer itemId, @Param("attributeId") Integer attributeId);
}
