package com.cassinisys.plm.repo.pqm;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.pqm.PQMNCRType;
import com.cassinisys.plm.model.pqm.PQMProblemReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface NCRTypeRepository extends JpaRepository<PQMNCRType, Integer> {
    List<PQMNCRType> findByIdIn(Iterable<Integer> ids);

    List<PQMNCRType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PQMNCRType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    @Query("select count (i) from PQMNCRType i where i.failureTypes.id= :lovId or i.severities.id= :lovId or i.dispositions.id= :lovId")
    Integer getUsedProblemReportsByLov(@Param("lovId") Integer lovId);

    @Query("select distinct i.failureTypes from PQMNCRType i")
    List<Lov> getUniqueNcrFailureTypes();

    @Query("select distinct i.severities from PQMNCRType i")
    List<Lov> getUniqueNcrSeverities();

    @Query("select distinct i.dispositions from PQMNCRType i")
    List<Lov> getUniqueNcrDispositions();

    PQMNCRType findByName(String name);

    PQMNCRType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PQMNCRType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<PQMNCRType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    @Query(value = "SELECT count(i) FROM pqm_ncr i JOIN pqm_ncr_type pnt ON pnt.id = i.ncr_type WHERE (pnt.failure_types= :lov AND i.failure_type= :lovValue) OR (pnt.severities= :lov AND i.severity= :lovValue) OR (pnt.dispositions= :lov AND i.disposition= :lovValue)", nativeQuery = true)
    Integer getNcrTypeLovValueCount(@Param("lov") Integer lov, @Param("lovValue") String lovValue);
}
