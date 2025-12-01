package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.InwardItem;
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
public interface InwardItemRepository extends JpaRepository<InwardItem, Integer>, QueryDslPredicateExecutor<InwardItem> {
    List<InwardItem> findByInward(Integer inward);

    List<InwardItem> findByInwardOrderByCreatedDateAsc(Integer inward);

    @Query("select i from InwardItem i where i.inward= :inward order by i.bomItem.item.itemMaster.itemName ASC")
    List<InwardItem> getInwardItemsOrderByItemName(@Param("inward") Integer inward);

    @Query("select i from InwardItem i where i.inward= :inward and i.bomItem.id= :bomItem")
    List<InwardItem> findByInwardAndBomItem(@Param("inward") Integer inward, @Param("bomItem") Integer bomItem);

    @Query("select i from InwardItem i where i.bomItem.id= :bomItem")
    List<InwardItem> findByBomItem(@Param("bomItem") Integer bomItem);

    @Query("select i from InwardItem i where i.bomItem.item.id= :revision")
    List<InwardItem> findByItemRevision(@Param("revision") Integer revision);

    @Query("SELECT i from InwardItem i where i.instancesCreated = false")
    List<InwardItem> getInstancesNotCreatedItems();
}
