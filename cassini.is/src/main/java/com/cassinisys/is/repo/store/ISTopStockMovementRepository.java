package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.procm.MovementType;
import com.cassinisys.is.model.store.ISTopStockMovement;
import com.cassinisys.is.model.store.ISTopStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Repository
public interface ISTopStockMovementRepository extends JpaRepository<ISTopStockMovement, Integer>, QueryDslPredicateExecutor<ISTopStockMovement> {

    List<ISTopStockMovement> findByItemAndStore(Integer itemId, ISTopStore store);

    List<ISTopStockMovement> findByStore(ISTopStore store);

    List<ISTopStockMovement> findByItem(Integer boqItem);

    List<ISTopStockMovement> findByItemAndProject(Integer boqItem, Integer projectId);

    List<ISTopStockMovement> findByProject(Integer projectId);

    @Query("SELECT MIN(h.timeStamp) from ISTopStockMovement h WHERE h.item IN :itemIds and h.store.id =:storeId")
    Date getMinimumDate(@Param("itemIds") List<Integer> itemIds, @Param("storeId") Integer storeId);

    List<ISTopStockMovement> findByItemInAndStoreAndTimeStampAfterAndTimeStampBeforeAndMovementTypeNot(List<Integer> items, ISTopStore store, Date afterDate, Date beforeDate, MovementType movementType);

    List<ISTopStockMovement> findByIdInAndTimeStampAfterAndTimeStampBefore(List<Integer> itemIds, Date afterDate, Date beforeDate);

    List<ISTopStockMovement> findByTimeStampAfterAndTimeStampBefore(Date afterDate, Date beforeDate);

    List<ISTopStockMovement> findByStoreAndMovementType(ISTopStore store, MovementType movementType);

    Page<ISTopStockMovement> findByStoreOrderByTimeStampDesc(ISTopStore store, Pageable pageable);

    List<ISTopStockMovement> findByIdInAndItemAndStoreAndTimeStampBeforeOrderByTimeStampDesc(List<Integer> ids, Integer itemId, ISTopStore store, Date date);

    @Query("SELECT MIN(h.timeStamp) from ISTopStockMovement h WHERE h.store.id =:storeId")
    Date getMinimumDate(@Param("storeId") Integer storeId);

    @Query("SELECT MAX(h.timeStamp) from ISTopStockMovement h WHERE h.store.id =:storeId")
    Date getMaximumDate(@Param("storeId") Integer storeId);

}
