package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.procm.MovementType;
import com.cassinisys.is.model.store.ISStockReturnItem;
import com.cassinisys.is.model.store.ISTopStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 05/12/18.
 */
@Repository
public interface ISStockReturnItemRepository extends JpaRepository<ISStockReturnItem, Integer>, QueryDslPredicateExecutor<ISStockReturnItem> {

    List<ISStockReturnItem> findByStockReturn(Integer stockReturnId);

    List<ISStockReturnItem> findByItemAndMovementType(Integer itemId, MovementType movementType);

    @Query("SELECT SUM(i.quantity) from ISStockReturnItem i where i.project= :projectId and i.item= :itemId and i.store = :store")
    Double getTotalReturnQuantityByProjectAndItemAndStore(@Param("projectId") Integer projectId, @Param("itemId") Integer itemId, @Param("store") ISTopStore store);

    @Query("SELECT SUM(i.quantity) from ISStockReturnItem i where i.item= :itemId and i.store = :store and i.project is null ")
    Double getTotalReturnQuantityByItemAndStoreAndProjectIsNull(@Param("itemId") Integer itemId, @Param("store") ISTopStore store);
}
