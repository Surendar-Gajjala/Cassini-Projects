package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMNCR;
import com.cassinisys.plm.model.pqm.PQMNCRType;
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
public interface NCRRepository extends JpaRepository<PQMNCR, Integer>, QueryDslPredicateExecutor<PQMNCR> {
    List<PQMNCR> findByIdIn(Iterable<Integer> ids);

    List<PQMNCR> findByNcrType(PQMNCRType type);

    PQMNCR findByNcrNumber(String ncrNumber);

    List<PQMNCR> findByReleasedTrueOrderByModifiedDateDesc();

    @Query("SELECT count(i) from PQMNCR i")
    Integer getTotalNcrs();

    @Query("SELECT count(i) FROM PQMNCR i where i.statusType= :statusType")
    Integer getRejectedNcrs(@Param("statusType") WorkflowStatusType statusType);

    @Query("SELECT count(i) FROM PQMNCR i where i.released = false and i.statusType != 'REJECTED'")
    Integer getPendingNcrs();

    @Query("SELECT count(i) FROM PQMNCR i where i.released = true")
    Integer getApprovedNcrs();

    @Query("select distinct i.severity from PQMNCR i order by i.severity asc")
    List<String> getUniqueSeverities();

    @Query("select distinct i.failureType from PQMNCR i order by i.failureType asc")
    List<String> getUniqueFailures();

    @Query("select distinct i.disposition from PQMNCR i order by i.disposition asc")
    List<String> getUniqueDispositions();

    @Query("SELECT count(i) FROM PQMNCR i where i.severity= :severity")
    Integer getNcrsBySeverity(@Param("severity") String severity);

    @Query("SELECT count(i) FROM PQMNCR i where i.failureType= :failureType")
    Integer getNcrsByFailureType(@Param("failureType") String failureType);

    @Query("SELECT count(i) FROM PQMNCR i where i.disposition= :disposition")
    Integer getNcrsByDisposition(@Param("disposition") String disposition);

    @Query("SELECT count(i) from PQMNCR i where i.isImplemented = true")
    Integer getImplementedNcrsCounts();

    @Query("select count (i) from PQMNCR i where (LOWER(CAST(i.ncrNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.ncrType.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.title as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.severity as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.disposition as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.failureType as text))) LIKE '%' || :searchText || '%'")
    Integer getNCRCountBySearchQuery(@Param("searchText") String searchText);
}
