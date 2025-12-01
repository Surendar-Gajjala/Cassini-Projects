package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.Request;
import com.cassinisys.drdo.model.transactions.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Integer>, QueryDslPredicateExecutor<Request> {

    @Query("SELECT i from Request i where i.status= :requestStatus and i.issued = false")
    List<Request> findAllFinishedRequests(@Param("requestStatus") RequestStatus requestStatus);

    @Query("SELECT i from Request i where i.bomInstance.id= :instance order by i.createdDate DESC")
    List<Request> getRequestsByInstance(@Param("instance") Integer instance);

    @Query("SELECT i from Request i where i.status= :status order by i.modifiedDate DESC")
    List<Request> getRequestsByStatus(@Param("status") RequestStatus status);

    @Query("SELECT i from Request i where i.status= :status and i.versity = :versity order by i.modifiedDate DESC")
    List<Request> getRequestsByStatusAndVersity(@Param("status") RequestStatus status, @Param("versity") Boolean versity);

    @Query("SELECT i from Request i where i.status= :status and i.versity = :versity and i.issued = false order by i.modifiedDate DESC")
    List<Request> getRequestsByStatusAndVersityAndIssued(@Param("status") RequestStatus status, @Param("versity") Boolean versity);

    @Query("SELECT i from Request i where i.status= :status and i.issued = false order by i.modifiedDate DESC")
    List<Request> getNotIssuedApproveRequestsByStatus(@Param("status") RequestStatus status);

    @Query("SELECT i from Request i where i.status != approved and i.status != rejected")
    List<Request> getNotApprovedAndRejectedRequests(@Param("approved") RequestStatus approved, @Param("rejected") RequestStatus rejected);

    @Query("SELECT i from Request i where i.status!= :status and i.issued = false")
    List<Request> getRequestsWithoutByStatus(@Param("status") RequestStatus status);

    @Query("SELECT i from Request i where i.requestedBy.id= :requestedBy")
    List<Request> getRequestedByRequests(@Param("requestedBy") Integer requestedBy);
}
