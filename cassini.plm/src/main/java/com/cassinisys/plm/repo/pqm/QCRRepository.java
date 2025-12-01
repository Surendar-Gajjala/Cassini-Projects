package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMQCR;
import com.cassinisys.plm.model.pqm.PQMQCRType;
import com.cassinisys.plm.model.pqm.QCRFor;
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
public interface QCRRepository extends JpaRepository<PQMQCR, Integer>, QueryDslPredicateExecutor<PQMQCR> {
    List<PQMQCR> findByIdIn(Iterable<Integer> ids);

    List<PQMQCR> findByQcrType(PQMQCRType type);

    List<PQMQCR> findByQcrForAndReleasedTrue(QCRFor qcrFor);

    List<PQMQCR> findByQcrFor(QCRFor qcrFor);

    PQMQCR findByQcrNumber(String number);

    List<PQMQCR> findByReleasedTrueOrderByModifiedDateDesc();

    @Query("select count(i) from PQMQCR i")
    Integer getTotalQcrs();

    @Query("SELECT count(i) FROM PQMQCR i where i.statusType= :statusType")
    Integer getRejectedQcrs(@Param("statusType") WorkflowStatusType statusType);

    @Query("SELECT count(i) FROM PQMQCR i where i.qcrFor= :qcrFor")
    Integer getQcrsByFor(@Param("qcrFor") QCRFor qcrFor);

    @Query("SELECT count(i) FROM PQMQCR i where i.released = false and i.statusType != 'REJECTED'")
    Integer getPendingQcrs();

    @Query("SELECT count(i) FROM PQMQCR i where i.released = true")
    Integer getApprovedQcrs();

    @Query("SELECT count(i) from PQMQCR i where i.isImplemented = true")
    Integer getImplementedQcrsCounts();

    @Query("select count (i) from PQMQCR i where (LOWER(CAST(i.qcrType.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.qcrNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.title as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getQCRCountBySearchQuery(@Param("searchText") String searchText);
}
