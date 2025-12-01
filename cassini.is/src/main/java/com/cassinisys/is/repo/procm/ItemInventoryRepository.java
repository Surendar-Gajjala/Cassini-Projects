package com.cassinisys.is.repo.procm;
/**
 * The Class is for ItemInventoryRepository
 **/

import com.cassinisys.is.model.procm.ISItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemInventoryRepository extends JpaRepository<ISItemInventory, Integer>,
        QueryDslPredicateExecutor<ISItemInventory> {
    /**
     * The method used to findByBoqItem from the list of ISItemInventory
     **/
    List<ISItemInventory> findByBoqItem(Integer boqItemId);

    /**
     * The method used to findByBoqItemIn from the list of ISItemInventory
     **/
    List<ISItemInventory> findByBoqItemIn(Iterable<Integer> boqItemIds);

    /**
     * The method used to findByStore from the list of ISItemInventory
     **/
    List<ISItemInventory> findByStore(Integer id);

    /**
     * The method used to findByProject from the list of ISItemInventory
     **/
    List<ISItemInventory> findByProject(Integer id);

    /**
     * The method used to findByStoreAndBoqItem of ISItemInventory
     **/
    ISItemInventory findByStoreAndBoqItem(Integer storeId, Integer itemId);

    List<ISItemInventory> findByStoreInAndBoqItemIn(Iterable<Integer> storeIds, Iterable<Integer> itemIds);
}
