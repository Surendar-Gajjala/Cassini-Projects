package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.ItemInstanceStatus;
import com.cassinisys.drdo.model.bom.ItemInstanceStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 01-11-2018.
 */
@Repository
public interface ItemInstanceStatusHistoryRepository extends JpaRepository<ItemInstanceStatusHistory, Integer>, QueryDslPredicateExecutor {

    @Query("SELECT i from ItemInstanceStatusHistory i where i.itemInstance.id= :itemInstance order by i.timestamp DESC")
    List<ItemInstanceStatusHistory> getItemInstanceHistory(@Param("itemInstance") Integer itemInstance);

    @Query("SELECT i from ItemInstanceStatusHistory i where i.itemInstance.id= :instanceId and i.status= :status")
    ItemInstanceStatusHistory getHistoryByInstanceAndStatus(@Param("instanceId") Integer instanceId, @Param("status") ItemInstanceStatus status);

    @Query("SELECT i from ItemInstanceStatusHistory i where i.itemInstance.id= :instanceId and i.status= :status order by i.timestamp desc")
    List<ItemInstanceStatusHistory> getInstanceHistoryByInstanceAndStatus(@Param("instanceId") Integer instanceId, @Param("status") ItemInstanceStatus status);

    @Query("SELECT i from ItemInstanceStatusHistory i where i.itemInstance.id= :instanceId and i.status= :status order by i.timestamp desc")
    List<ItemInstanceStatusHistory> getReviewOrPacceptHistoryByInstanceAndStatus(@Param("instanceId") Integer instanceId, @Param("status") ItemInstanceStatus status);
}
