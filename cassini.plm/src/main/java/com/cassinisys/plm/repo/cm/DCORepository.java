package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMDCO;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface DCORepository extends JpaRepository<PLMDCO, Integer>, QueryDslPredicateExecutor<PLMDCO> {

    List<PLMDCO> findByIdIn(Iterable<Integer> ids);

    PLMDCO findByDcoNumber(String dcoNumber);

    List<PLMDCO> findByDcoType(Integer type);

    @Query("select count(i) from PLMDCO i")
    Integer getTotalDcos();

    @Query("select count(i) from PLMDCO i where i.isReleased = true")
    Integer getReleasedDcos();

    @Query("select count(i) from PLMDCO i where i.statusType= :statusType")
    Integer getRejectedDcos(@Param("statusType") WorkflowStatusType statusType);

    @Query("select count(i) from PLMDCO i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and (i.statusType= :statusType or i.statusType= :undefined) " +
            "and w.started = true and w.onhold = false")
    Integer getPendingDcos(@Param("statusType") WorkflowStatusType statusType, @Param("undefined") WorkflowStatusType undefined);

    @Query("select count(i) from PLMDCO i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and w.onhold = true")
    Integer getOnHoldDcos();

    @Query("select count(i) from PLMDCO i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and w.started = false")
    Integer getNotStartedDcos();

    @Query(
            "SELECT i FROM PLMDCO i WHERE i.dcoType IN :typeIds"
    )
    Page<PLMDCO> getByDcoTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select count (i) from PLMDCO i where (LOWER(CAST(i.dcoNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.title as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getDCOCountBySearchQuery(@Param("searchText") String searchText);

    @Query("select distinct i.changeAnalyst from PLMDCO i")
    List<Integer> getChangeAnalystIds();

    @Query("select distinct i.status from PLMDCO i")
    List<String> getStatusIds();
}
