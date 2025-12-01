package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMMCO;
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
 * Created by subramanyamreddy on 023 23-Jun -17.
 */
@Repository
public interface MCORepository extends JpaRepository<PLMMCO, Integer>, QueryDslPredicateExecutor<PLMMCO> {

    List<PLMMCO> findByIdIn(Iterable<Integer> ids);

    PLMMCO findByMcoNumber(String mcoNumber);

    @Query("select i from PLMMCO i where i.mcoType.id= :mcoType")
    List<PLMMCO> findByMcoType(@Param("mcoType") Integer mcoType);

    @Query("select count(i) from PLMMCO i")
    Integer getTotalMcos();

    @Query("select count(i) from PLMMCO i where i.released = true")
    Integer getReleasedDcos();

    @Query("select count(i) from PLMMCO i where i.statusType= :statusType")
    Integer getRejectedDcos(@Param("statusType") WorkflowStatusType statusType);

    @Query("select count(i) from PLMMCO i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and (i.statusType= :statusType or i.statusType= :undefined) " +
            "and w.started = true and w.onhold = false")
    Integer getPendingDcos(@Param("statusType") WorkflowStatusType statusType, @Param("undefined") WorkflowStatusType undefined);

    @Query("select count(i) from PLMMCO i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and w.onhold = true")
    Integer getOnHoldMcos();

    @Query("select count(i) from PLMMCO i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and w.started = false")
    Integer getNotStartedMcos();

    @Query(
            "SELECT i FROM PLMMCO i WHERE i.mcoType.id IN :typeIds"
    )
    Page<PLMMCO> getByMcoTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select count (i) from PLMMCO i where (LOWER(CAST(i.mcoNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.title as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.mcoType.name as text))) LIKE '%' || :searchText || '%'")
    Integer getMCOCountBySearchQuery(@Param("searchText") String searchText);

    @Query("select distinct i.changeAnalyst from PLMMCO i")
    List<Integer> getChangeAnalystIds();

    @Query("select distinct i.status from PLMMCO i")
    List<String> getStatusIds();

}
