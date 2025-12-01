package com.cassinisys.plm.repo.pqm;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.pqm.PQMMaterialInspectionPlanType;
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
public interface ProblemReportTypeRepository extends JpaRepository<PQMProblemReportType, Integer> {
    List<PQMProblemReportType> findByIdIn(Iterable<Integer> ids);

    List<PQMProblemReportType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PQMProblemReportType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    @Query("select count (i) from PQMProblemReportType i where i.failureTypes.id= :lovId or i.severities.id= :lovId or i.dispositions.id= :lovId")
    Integer getUsedProblemReportsByLov(@Param("lovId") Integer lovId);

    @Query("select distinct i.failureTypes from PQMProblemReportType i")
    List<Lov> getUniqueProblemReportFailureTypes();

    @Query("select distinct i.severities from PQMProblemReportType i")
    List<Lov> getUniqueProblemReportSeverities();

    @Query("select distinct i.dispositions from PQMProblemReportType i")
    List<Lov> getUniqueProblemReportDispositions();

    PQMProblemReportType findByName(String s);

    PQMProblemReportType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PQMProblemReportType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<PQMProblemReportType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);
    @Query(value = "SELECT count(i) FROM pqm_problem_report i JOIN pqm_problem_report_type pprt ON pprt.id = i.pr_type WHERE (pprt.failure_types= :lov AND i.failure_type= :lovValue) OR (pprt.severities= :lov AND i.severity= :lovValue) OR (pprt.dispositions= :lov AND i.disposition= :lovValue)", nativeQuery = true)
    Integer getPrTypeLovValueCount(@Param("lov") Integer lov, @Param("lovValue") String lovValue);
}
