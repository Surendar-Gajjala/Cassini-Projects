package com.cassinisys.drdo.repo.inventory;

import com.cassinisys.drdo.model.bom.ItemRevision;
import com.cassinisys.drdo.model.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>, QueryDslPredicateExecutor<Inventory> {

    Inventory findByBomAndItem(Integer bom, ItemRevision item);

    @Query("SELECT i from Inventory i where i.bom= :bom and i.uniqueCode= :uniqueCode and i.section= :section")
    Inventory getInventoryByBomAndUniqueCodeAndSection(@Param("bom") Integer bom, @Param("uniqueCode") String uniqueCode, @Param("section") Integer section);

    @Query("SELECT i from Inventory i where i.bom= :bom and i.item.id = :item and i.uniqueCode= :uniqueCode and i.section= :section")
    Inventory getInventoryByBomAndItemAndUniqueCodeAndSection(@Param("bom") Integer bom, @Param("item") Integer item, @Param("uniqueCode") String uniqueCode, @Param("section") Integer section);

    @Query("SELECT i from Inventory i where i.bom= :bom and i.uniqueCode= :uniqueCode and i.section is null")
    Inventory getInventoryByBomAndUniqueCodeAndSectionIsNull(@Param("bom") Integer bom, @Param("uniqueCode") String uniqueCode);

    @Query("SELECT i from Inventory i where i.bom= :bom and i.item.id = :item and i.uniqueCode= :uniqueCode and i.section is null")
    Inventory getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(@Param("bom") Integer bom, @Param("item") Integer item, @Param("uniqueCode") String uniqueCode);
}
