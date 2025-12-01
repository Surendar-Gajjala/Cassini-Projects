package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.Request;
import com.cassinisys.drdo.model.transactions.RequestItem;
import com.cassinisys.drdo.model.transactions.RequestItemStatus;
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
public interface RequestItemRepository extends JpaRepository<RequestItem, Integer>, QueryDslPredicateExecutor<RequestItem> {

    List<RequestItem> findByRequest(Request request);

    @Query("SELECT i from RequestItem i where i.item.id= :item and i.request.bomInstance.id= :bomInstance and i.status!= :status")
    List<RequestItem> getNotRejectedRequestItems(@Param("item") Integer item, @Param("bomInstance") Integer bomInstance, @Param("status") RequestItemStatus status);

    @Query("SELECT i from RequestItem i where i.request.bomInstance.id= :bom and i.item.id= :item and i.status = 'APPROVED'")
    List<RequestItem> getRequestedByInstanceAndItem(@Param("bom") Integer bom, @Param("item") Integer item);

    @Query("SELECT i from RequestItem i where i.request.id= :request and i.status = 'PENDING'")
    List<RequestItem> getPendingItems(@Param("request") Integer request);

    @Query("SELECT i from RequestItem i where i.request.id= :request and (i.status = 'PENDING' or i.status = 'ACCEPTED')")
    List<RequestItem> getNotApprovedItems(@Param("request") Integer request);

    @Query("SELECT i from RequestItem i where i.request.id= :request and i.status = 'APPROVED'")
    List<RequestItem> getApprovedItems(@Param("request") Integer request);

    @Query("SELECT i from RequestItem i where i.request.id= :request and i.status = 'ACCEPTED'")
    List<RequestItem> getAcceptedItems(@Param("request") Integer request);

    @Query("SELECT i from RequestItem i where i.request.id= :request and i.status = 'REJECTED'")
    List<RequestItem> getRejectedItems(@Param("request") Integer request);

    @Query("SELECT i from RequestItem i where i.status = 'APPROVED'")
    List<RequestItem> getAllApprovedItems();

    @Query("SELECT i from RequestItem i where i.item.id= :item")
    List<RequestItem> getRequestItemsByItem(@Param("item") Integer item);

    @Query("SELECT i from RequestItem i where i.status= :status")
    List<RequestItem> getRequestItemsByStatus(@Param("status") RequestItemStatus status);

    @Query("SELECT i from RequestItem i where i.request.id= :requestId and i.item.item.itemMaster.itemName= :itemName")
    RequestItem getRequestItemByItemName(@Param("requestId") Integer requestId, @Param("itemName") String itemName);

    @Query("SELECT i from RequestItem i where i.request.bomInstance.id= :instanceId and i.item.id= :bomInstanceItem")
    List<RequestItem> getRequestItemByBomInstanceAndBomInstanceItem(@Param("instanceId") Integer instanceId, @Param("bomInstanceItem") Integer bomInstanceItem);
}
