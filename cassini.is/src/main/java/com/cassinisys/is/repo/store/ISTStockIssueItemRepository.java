package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.procm.MovementType;
import com.cassinisys.is.model.store.ISTStockIssueItem;
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
public interface ISTStockIssueItemRepository extends JpaRepository<ISTStockIssueItem, Integer>, QueryDslPredicateExecutor<ISTStockIssueItem> {

    List<ISTStockIssueItem> findByItem(Integer boqItem);
//    @Query(
//            "SELECT i FROM ISTStockIssueItem i WHERE i.item.id IN :typeIds"
//    )
//    List<ISTStockIssueItem> findByItemIds(@Param("typeIds") List<Integer> typeId);

    List<ISTStockIssueItem> findByStoreAndItem(ISTopStore store, Integer boqItem);

    List<ISTStockIssueItem> findByIssue(Integer issue);

    List<ISTStockIssueItem> findByMovementTypeAndItem(MovementType type, Integer item);

    @Query("SELECT SUM(i.quantity) from ISTStockIssueItem i where i.project= :projectId and i.item= :itemId and i.store = :store")
    Double getTotalIssuedQuantityByProjectAndItemAndStore(@Param("projectId") Integer projectId, @Param("itemId") Integer itemId, @Param("store") ISTopStore store);

    @Query("SELECT MIN(h.timeStamp) from ISTStockIssueItem h WHERE h.store.id =:storeId")
    Date getMinimumDate(@Param("storeId") Integer storeId);

    List<ISTStockIssueItem> findByStoreAndTimeStampAfterAndTimeStampBefore(ISTopStore store, Date startDate, Date endDate);

    List<ISTStockIssueItem> findByIssueAndTimeStampAfterAndTimeStampBefore(Integer issue, Date startDate, Date endDate);

    Page<ISTStockIssueItem> findByStore(ISTopStore storeId, Pageable pageable);

}
