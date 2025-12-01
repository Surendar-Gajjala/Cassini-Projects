package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMECO;
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
 * Created by lakshmi on 1/3/2016.
 */
@Repository
public interface ECORepository extends JpaRepository<PLMECO, Integer>, QueryDslPredicateExecutor<PLMECO> {
    PLMECO findById(Integer id);

    List<PLMECO> findByIdIn(Iterable<Integer> ids);

    List<PLMECO> findByEcoNumber(String ecoNumber);

    List<PLMECO> findByStatusTypeNot(WorkflowStatusType statusType);

    Page<PLMECO> findByReleasedTrue(Pageable pageable);

    Page<PLMECO> findByReleasedFalse(Pageable pageable);

    List<PLMECO> findByEcoType(Integer type);

    @Query(
            "SELECT i FROM PLMECO i WHERE i.ecoType IN :typeIds"
    )
    Page<PLMECO> getByEcoTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select count(i) from PLMECO i")
    Integer getTotalEcos();

    @Query("select count(i) from PLMECO i where i.released = true and i.cancelled = false")
    Integer getReleasedEcos();

    @Query("select count(i) from PLMECO i where i.statusType= :statusType")
    Integer getRejectedEcos(@Param("statusType") WorkflowStatusType statusType);

    @Query("select count(i) from PLMECO i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and (i.statusType= :statusType or i.statusType= :undefined) " +
            "and w.started = true and w.onhold = false")
    Integer getPendingEcos(@Param("statusType") WorkflowStatusType statusType, @Param("undefined") WorkflowStatusType undefined);

    @Query("select count(i) from PLMECO i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and w.onhold = true")
    Integer getOnHoldEcos();

    @Query("select count(i) from PLMECO i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and w.started = false")
    Integer getNotStartedEcos();

    @Query("select count (i) from PLMECO i where (LOWER(CAST(i.ecoNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.title as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getECOCountBySearchQuery(@Param("searchText") String searchText);

    @Query("select distinct i.ecoOwner from PLMECO i")
    List<Integer> getChangeAnalystIds();

    @Query("select distinct i.status from PLMECO i")
    List<String> getStatusIds();
    
}
