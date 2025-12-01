package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.ItemInstanceStatus;
import com.cassinisys.drdo.model.transactions.InwardItemInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 15-10-2018.
 */
@Repository
public interface InwardItemInstanceRepository extends JpaRepository<InwardItemInstance, Integer>, QueryDslPredicateExecutor<InwardItemInstance> {
    List<InwardItemInstance> findByInwardItem(Integer inwardItem);

    @Query("SELECT i FROM InwardItemInstance i where i.item.oemNumber= :oemNumber")
    InwardItemInstance findOemNumberInstance(@Param("oemNumber") String oemNumber);

    @Query("SELECT i FROM InwardItemInstance i where i.item.oemNumber= :oemNumber and i.item.manufacturer.mfrCode= :mfrCode")
    InwardItemInstance findOemNumberInstanceByMfrCode(@Param("oemNumber") String oemNumber, @Param("mfrCode") String mfrCode);

    @Query("SELECT i FROM InwardItemInstance i where i.item.upnNumber= :upnNumber")
    InwardItemInstance findUpnNumberInstance(@Param("upnNumber") String upnNumber);

    @Query("SELECT i from InwardItemInstance i where i.inwardItem= :inwardItemId and (i.item.status = 'ACCEPT' or i.item.status = 'P_ACCEPT') and i.latest = true")
    List<InwardItemInstance> getAcceptedItemInstances(@Param("inwardItemId") Integer inwardItemId);

    @Query("SELECT i from InwardItemInstance i where i.inwardItem= :inwardItemId and (i.item.status = 'NEW'" +
            " or i.item.status = 'ACCEPT' or i.item.status = 'P_ACCEPT' or i.item.status = 'INVENTORY' or i.item.status = 'REVIEW' or i.item.status = 'REVIEWED') and i.latest = true")
    List<InwardItemInstance> getOnHoldInwardInstances(@Param("inwardItemId") Integer inwardItemId);

    @Query("SELECT i from InwardItemInstance i where i.inwardItem= :inwardItemId and i.item.status = 'REJECTED' and i.latest = true")
    List<InwardItemInstance> getReturnedInwardInstances(@Param("inwardItemId") Integer inwardItemId);

    @Query("SELECT i from InwardItemInstance i where i.inwardItem= :inwardItemId and i.item.status = 'FAILURE' and i.latest = true")
    List<InwardItemInstance> getFailedInwardInstances(@Param("inwardItemId") Integer inwardItemId);

    @Query("SELECT i from InwardItemInstance i where i.inwardItem= :inwardItem and i.item.status= :status and i.latest = true")
    List<InwardItemInstance> getItemsToDispatch(@Param("inwardItem") Integer inwardItem, @Param("status") ItemInstanceStatus status);

    @Query("SELECT i from InwardItemInstance i where i.inwardItem= :inwardItem and i.item.status= :status and i.latest = true")
    List<InwardItemInstance> getInwardItemInstancesByStatus(@Param("inwardItem") Integer inwardItem, @Param("status") ItemInstanceStatus status);

    @Query("SELECT i from InwardItemInstance i where i.inwardItem= :inwardItem and i.item.provisionalAccept = true and i.latest = true")
    List<InwardItemInstance> getProvisionallyAcceptedInwardItemInstances(@Param("inwardItem") Integer inwardItem);

    @Query("SELECT i from InwardItemInstance i where i.inwardItem= :inwardItem and (i.item.status= :pStatus or i.item.status= :rStatus)")
    List<InwardItemInstance> getProvisionalOrReviewInstances(@Param("inwardItem") Integer inwardItem, @Param("pStatus") ItemInstanceStatus pStatus, @Param("rStatus") ItemInstanceStatus rStatus);

    @Query("SELECT i from InwardItemInstance i where i.inwardItem= :inwardItem and (i.item.status= :aStatus or i.item.status= :iStatus)")
    List<InwardItemInstance> getAcceptOrInventoryInstances(@Param("inwardItem") Integer inwardItem, @Param("aStatus") ItemInstanceStatus aStatus, @Param("iStatus") ItemInstanceStatus iStatus);

    @Query("SELECT i from InwardItemInstance i where i.inwardItem= :inwardItem and (i.item.status= :pStatus or i.item.status= :sStatus and i.latest = true)")
    List<InwardItemInstance> getProvisionalOrSubmittedInstances(@Param("inwardItem") Integer inwardItem, @Param("pStatus") ItemInstanceStatus pStatus,
                                                                @Param("sStatus") ItemInstanceStatus sStatus);

    @Query("SELECT i from InwardItemInstance i where i.item.id= :item order by i.latest ASC")
    List<InwardItemInstance> getInwardItemInstanceByInstance(@Param("item") Integer item);

    @Query("SELECT i from InwardItemInstance i where i.item.id= :item and i.latest = true")
    List<InwardItemInstance> getInwardItemInstancesByInstance(@Param("item") Integer item);

    @Query("SELECT i from InwardItemInstance i where i.item.id= :item and i.latest = false ")
    List<InwardItemInstance> getInwardItemInstancesByInstanceAndLatestFalse(@Param("item") Integer item);

    List<InwardItemInstance> findByInwardItemAndLatestTrue(Integer inwardItem);

    InwardItemInstance findByItem(ItemInstance itemInstance);
}
