package com.cassinisys.drdo.repo.inventory;

import com.cassinisys.drdo.model.inventory.ItemAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 03-12-2018.
 */
@Repository
public interface ItemAllocationRepository extends JpaRepository<ItemAllocation, Integer>, QueryDslPredicateExecutor<ItemAllocation> {

    List<ItemAllocation> findByBomInstance(Integer bomInstance);

    ItemAllocation findByBomAndBomInstanceAndBomInstanceItem(Integer bom, Integer bomInstance, Integer bomInstanceItem);

    ItemAllocation findByBomInstanceAndBomInstanceItem(Integer bomInstance, Integer bomInstanceItem);

    ItemAllocation findByBomInstanceItem(Integer bomInstanceItem);
}
