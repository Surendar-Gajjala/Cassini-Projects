package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.procm.MovementType;
import com.cassinisys.is.model.store.ISStockReceiveItem;
import com.cassinisys.is.model.store.ISTopStore;
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
public interface ISStockReceiveItemRepository extends JpaRepository<ISStockReceiveItem, Integer>, QueryDslPredicateExecutor<ISStockReceiveItem> {

    List<ISStockReceiveItem> findByStore(ISTopStore store);

    List<ISStockReceiveItem> findByStoreAndTimeStampAfterAndTimeStampBefore(ISTopStore store, Date startDate, Date endDate);

    List<ISStockReceiveItem> findByStoreAndItem(ISTopStore store, Integer boqItem);

    List<ISStockReceiveItem> findByItemIn(Iterable<Integer> itemsIds);

    List<ISStockReceiveItem> findByItem(Integer boqItem);

    List<ISStockReceiveItem> findByReceive(Integer receive);

    List<ISStockReceiveItem> findByMovementTypeAndItem(MovementType type, Integer item);

    @Query("SELECT MIN(h.timeStamp) from ISStockReceiveItem h WHERE h.store.id =:storeId")
    Date getMinimumDate(@Param("storeId") Integer storeId);

    List<ISStockReceiveItem> findByReceiveAndTimeStampAfterAndTimeStampBefore(Integer receive, Date startDate, Date endDate);
}
