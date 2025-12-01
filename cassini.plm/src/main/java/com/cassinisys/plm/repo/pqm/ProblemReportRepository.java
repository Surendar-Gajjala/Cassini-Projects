package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMProblemReport;
import com.cassinisys.plm.model.pqm.PQMProblemReportType;
import com.cassinisys.plm.model.pqm.ReporterType;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface ProblemReportRepository extends JpaRepository<PQMProblemReport, Integer>, QueryDslPredicateExecutor<PQMProblemReport> {

    List<PQMProblemReport> findByIdIn(Iterable<Integer> ids);

    List<PQMProblemReport> findByPrType(PQMProblemReportType problemReportType);

    PQMProblemReport findByPrNumber(String prNumber);

    List<PQMProblemReport> findByReporterTypeAndReportedBy(ReporterType reporterType, Integer reportedBy);

    List<PQMProblemReport> findByReporterType(ReporterType reporterType);

    List<PQMProblemReport> findByReleasedTrueOrderByModifiedDateDesc();

    List<PQMProblemReport> findByProduct(Integer product);

    @Query("select count(i) from PQMProblemReport i")
    Integer getTotalPrs();

    @Query("SELECT count(i) FROM PQMProblemReport i where i.statusType= :statusType")
    Integer getRejectedPrs(@Param("statusType") WorkflowStatusType statusType);

    @Query("SELECT count(i) FROM PQMProblemReport i where i.reportedBy= :reportedBy")
    Integer getPrsByReportedBy(@Param("reportedBy") Integer reportedBy);

    @Query("SELECT count(i) FROM PQMProblemReport i where i.reporterType= :reporterType")
    Integer getPrsByReporterType(@Param("reporterType") ReporterType reporterType);

    @Query("SELECT count(i) FROM PQMProblemReport i where i.released = true")
    Integer getApprovedPrs();

    @Query("SELECT count(i) FROM PQMProblemReport i where i.released = false and i.statusType != 'REJECTED'")
    Integer getPendingPrs();

    @Query("select distinct i.severity from PQMProblemReport i order by i.severity asc")
    List<String> getUniqueSeverities();

    @Query("select distinct i.failureType from PQMProblemReport i order by i.failureType asc")
    List<String> getUniqueFailures();

    @Query("select distinct i.disposition from PQMProblemReport i order by i.disposition asc")
    List<String> getUniqueDispositions();

    @Query("SELECT count(i) FROM PQMProblemReport i where i.severity= :severity")
    Integer getPrsBySeverity(@Param("severity") String severity);

    @Query("SELECT count(i) FROM PQMProblemReport i where i.failureType= :failureType")
    Integer getPrsByFailureType(@Param("failureType") String failureType);

    @Query("SELECT count(i) FROM PQMProblemReport i where i.disposition= :disposition")
    Integer getPrsByDisposition(@Param("disposition") String disposition);

    @Query("SELECT count(i) from PQMProblemReport i where i.isImplemented = true")
    Integer getImplementedPrsCounts();

    @Query(value = "select i.reported_by,count(i.reported_by) from pqm_problem_report i where i.reporter_type = 'CUSTOMER' group by i.reported_by order by count (i.reported_by) desc LIMIT 10", nativeQuery = true)
    List<Object[]> getCustomerReportingProblems();

    @Query(value = "select i.product,count(i.product) from pqm_problem_report i where i.product is not null group by i.product order by count (i.product) desc LIMIT 10", nativeQuery = true)
    List<Object[]> getProductProblems();

    @Query("select count (i) from PQMProblemReport i where i.product= :itemId")
    Integer getPrCountByItem(@Param("itemId") Integer itemId);

    @Query("select count (i) from PQMProblemReport i where (LOWER(CAST(i.prNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.prType.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.problem as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.failureType as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.severity as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.disposition as text))) LIKE '%' || :searchText || '%' or i.product in :productIds")
    Integer getProblemReportsCountBySearchQueryOrProductIds(@Param("searchText") String searchText, @Param("productIds") Iterable<Integer> productIds);

    @Query("select count (i) from PQMProblemReport i where (LOWER(CAST(i.prNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.prType.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.problem as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.failureType as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.severity as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.disposition as text))) LIKE '%' || :searchText || '%'")
    Integer getProblemReportsCountBySearchQuery(@Param("searchText") String searchText);

}
