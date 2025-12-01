package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMECR;
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
public interface ECRRepository extends JpaRepository<PLMECR, Integer>, QueryDslPredicateExecutor<PLMECR> {
    List<PLMECR> findByIdIn(Iterable<Integer> ids);

    @Query("select i from PLMECR i where i.qcr.id= :id")
    List<PLMECR> findByQcr(@Param("id") Integer id);

    PLMECR findByCrNumber(String crNumber);

    List<PLMECR> findByCrType(Integer crType);

    PLMECR findByTitle(String title);

    @Query("select count(i) from PLMECR i")
    Integer getTotalEcrs();

    @Query("select count(i) from PLMECR i where i.isApproved = true")
    Integer getReleasedEcrs();

    @Query("select count(i) from PLMECR i where i.statusType= :statusType")
    Integer getRejectedEcrs(@Param("statusType") WorkflowStatusType statusType);

    @Query("select count(i) from PLMECR i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and (i.statusType= :statusType or i.statusType= :undefined) " +
            "and w.started = true and w.onhold = false")
    Integer getPendingEcrs(@Param("statusType") WorkflowStatusType statusType, @Param("undefined") WorkflowStatusType undefined);

    @Query("select count(i) from PLMECR i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and w.onhold = true")
    Integer getOnHoldEcrs();

    @Query("select count(i) from PLMECR i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and w.started = false")
    Integer getNotStartedEcrs();

    @Query(
            "SELECT i FROM PLMECR i WHERE i.crType IN :typeIds"
    )
    Page<PLMECR> getByDcoTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select count (i) from PLMECR i where (LOWER(CAST(i.crNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.title as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.changeReasonType as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.descriptionOfChange as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.reasonForChange as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.proposedChanges as text))) LIKE '%' || :searchText || '%'")
    Integer getECRCountBySearchQuery(@Param("searchText") String searchText);

    @Query("select distinct i.changeAnalyst from PLMECR i")
    List<Integer> getChangeAnalystIds();

    @Query("select distinct i.status from PLMECR i")
    List<String> getStatusIds();

    @Query("select distinct i.urgency from PLMECR i")
    List<String> getUrgencyIds();

    @Query("select distinct i.originator from PLMECR i")
    List<Integer> getOriginatorIds();

    @Query("select distinct i.requestedBy from PLMECR i where i.requestedBy is not Null")
    List<Integer> getRequesterIds();

    @Query("select distinct i.changeReasonType from PLMECR i")
    List<String> getChangeReasonTypeIds();
}
