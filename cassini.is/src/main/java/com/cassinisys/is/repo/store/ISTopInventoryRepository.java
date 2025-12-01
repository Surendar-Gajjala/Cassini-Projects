package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISTopInventory;
import com.cassinisys.is.model.store.ISTopStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Repository
public interface ISTopInventoryRepository extends JpaRepository<ISTopInventory, Integer>, QueryDslPredicateExecutor<ISTopInventory> {

    List<ISTopInventory> findByIdIn(Iterable<Integer> ids);

    List<ISTopInventory> findByStore(ISTopStore store);

    List<ISTopInventory> findByItem(Integer item);

    List<ISTopInventory> findByItemAndStore(Integer itemId, ISTopStore store);

    List<ISTopInventory> findByStoreInAndItemIn(Iterable<ISTopStore> topStores, Iterable<Integer> itemIds);

    List<ISTopInventory> findByItemIn(List<Integer> boqItem);

    List<ISTopInventory> findByItemAndProject(Integer boqItem, Integer projectId);

    List<ISTopInventory> findByProjectAndItemIn(Integer projectId, List<Integer> itemIds);

    List<ISTopInventory> findByStoreAndProjectAndItemIn(ISTopStore store, Integer projectId, List<Integer> itemIds);

    ISTopInventory findByStoreAndItemAndProject(ISTopStore storeId, Integer item, Integer projectId);

    @Query("SELECT DISTINCT(i.item) from ISTopInventory i where i.store.id= :store order by item")
    List<Integer> findDisItemsBystores(@Param("store") Integer store);

    @Query("SELECT DISTINCT(i.item) from ISTopInventory i ")
    List<Integer> findDisItems();

    @Query("SELECT DISTINCT(i.item) from ISTopInventory i where i.store.id= :store")
    List<Integer> findDistinctStoreItems(@Param("store") Integer store);

    @Query("SELECT DISTINCT(i.store) from ISTopInventory i ")
    List<ISTopStore> findDisStores();

    List<ISTopInventory> findByStoreAndProjectIsNull(ISTopStore store);

    ISTopInventory findByStoreAndItemAndProjectIsNull(ISTopStore store, Integer item);

    List<ISTopInventory> findByStoreAndProject(ISTopStore store, Integer projectId);

    Page<ISTopInventory> findByStoreAndProjectOrderByItem(ISTopStore store, Integer projectId, Pageable pageable);

    List<ISTopInventory> findByProject(Integer projectId);

    @Query("SELECT SUM(i.storeOnHand) from ISTopInventory i where i.store.id= :store and i.item = :item")
    Double getItemInventory(@Param("store") Integer store, @Param("item") Integer item);
}
