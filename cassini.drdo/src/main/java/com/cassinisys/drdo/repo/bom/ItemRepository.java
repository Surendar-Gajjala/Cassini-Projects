package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.Item;
import com.cassinisys.drdo.model.bom.ItemType;
import com.cassinisys.drdo.model.bom.ItemTypeSpecs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 04-10-2018.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>, QueryDslPredicateExecutor<Item> {

    List<Item> findByItemType(ItemType itemType);

    List<Item> findByItemTypeIdIn(Iterable<Integer> typeIds);

    Item findByItemNumberEqualsIgnoreCase(String itemNumber);

    Item findByItemName(String itemName);

    Item findByItemTypeAndItemNameEqualsIgnoreCase(ItemType itemType, String Name);

    List<Item> findByPartSpec(ItemTypeSpecs partSpec);

    Item findByLatestRevision(Integer revision);

    Item findByItemCodeAndPartSpec(String code, ItemTypeSpecs typeSpecs);



}
