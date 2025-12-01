package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMDCR;
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
public interface DCRRepository extends JpaRepository<PLMDCR, Integer>, QueryDslPredicateExecutor<PLMDCR> {

    List<PLMDCR> findByIdIn(Iterable<Integer> ids);

    PLMDCR findByCrNumber(String number);

    List<PLMDCR> findByCrType(Integer typeId);

    @Query("select count(i) from PLMDCR i")
    Integer getTotalDcrs();

    @Query("select count(i) from PLMDCR i where i.isApproved = true")
    Integer getReleasedDcrs();

    @Query("select count(i) from PLMDCR i where i.statusType= :statusType")
    Integer getRejectedDcrs(@Param("statusType") WorkflowStatusType statusType);

    @Query("select count(i) from PLMDCR i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and (i.statusType= :statusType or i.statusType= :undefined)" +
            " and w.started = true and w.onhold = false")
    Integer getPendingDcrs(@Param("statusType") WorkflowStatusType statusType, @Param("undefined") WorkflowStatusType undefined);

    @Query("select count(i) from PLMDCR i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and w.onhold = true")
    Integer getOnHoldDcrs();

    @Query("select count(i) from PLMDCR i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and w.started = false")
    Integer getNotStartedDcrs();

    @Query(
            "SELECT i FROM PLMDCR i WHERE i.crType IN :typeIds"
    )
    Page<PLMDCR> getByDcrTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select count (i) from PLMDCR i where (LOWER(CAST(i.crNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.title as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.descriptionOfChange as text))) LIKE '%' || :searchText || '%'")
    Integer getDCRCountBySearchQuery(@Param("searchText") String searchText);


    @Query("select distinct i.changeAnalyst from PLMDCR i")
    List<Integer> getChangeAnalystIds();

    @Query("select distinct i.status from PLMDCR i")
    List<String> getStatusIds();

    @Query("select distinct i.urgency from PLMDCR i")
    List<String> getUrgencyIds();

    @Query("select distinct i.originator from PLMDCR i")
    List<Integer> getOriginatorIds();

    @Query("select distinct i.requestedBy from PLMDCR i")
    List<Integer> getRequesterIds();

    @Query("select distinct i.changeReasonType from PLMDCR i")
    List<String> getChangeReasonTypeIds();

}
