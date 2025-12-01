package com.cassinisys.drdo.repo.inventory;

import com.cassinisys.drdo.model.inventory.StorageItem;
import com.cassinisys.drdo.model.inventory.StorageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subra on 07-10-2018.
 */
@Repository
public interface StorageItemRepository extends JpaRepository<StorageItem, Integer> {

    @Query("SELECT i from StorageItem i where i.storage.id= :storage")
    List<StorageItem> findByStorage(@Param("storage") Integer storage);

    @Query("SELECT i from StorageItem i where i.storage.id= :storage and i.uniqueCode= :uniqueCode and i.section is null")
    StorageItem findByStorageAndUniqueCodeAndSectionIsNull(@Param("storage") Integer storage, @Param("uniqueCode") String uniqueCode);

    @Query("SELECT i from StorageItem i where i.storage.id= :storage and i.uniqueCode= :uniqueCode and i.section.id= :section")
    StorageItem findByStorageAndUniqueCodeAndSection(@Param("storage") Integer storage, @Param("uniqueCode") String uniqueCode, @Param("section") Integer section);

    @Query("SELECT i from StorageItem i where i.storage.bom.id= :bom and i.uniqueCode= :uniqueCode and i.storage.onHold = true " +
            "and i.storage.returned = false and i.storage.isLeafNode = true and i.section.id= :section")
    List<StorageItem> getItemOnHoldStoragesByBomAndSection(@Param("bom") Integer bom, @Param("uniqueCode") String uniqueCode, @Param("section") Integer section);

    @Query("SELECT i from StorageItem i where i.storage.bom.id= :bom and i.uniqueCode= :uniqueCode and i.storage.onHold = true " +
            "and i.storage.returned = false and i.storage.isLeafNode = true and i.section is null")
    List<StorageItem> getItemOnHoldStoragesByBomAndSectionIsNull(@Param("bom") Integer bom, @Param("uniqueCode") String uniqueCode);

    @Query("SELECT i from StorageItem i where i.storage.bom.id= :bom and i.uniqueCode= :uniqueCode and i.storage.onHold = false " +
            "and i.storage.returned = true and i.storage.isLeafNode = true and i.section.id= :section")
    List<StorageItem> getItemReturnStoragesByBomAndSection(@Param("bom") Integer bom, @Param("uniqueCode") String uniqueCode, @Param("section") Integer section);

    @Query("SELECT i from StorageItem i where i.storage.bom.id= :bom and i.uniqueCode= :uniqueCode and i.storage.onHold = false " +
            "and i.storage.returned = true and i.storage.isLeafNode = true and i.section is null")
    List<StorageItem> getItemReturnStoragesByBomAndSectionIsNull(@Param("bom") Integer bom, @Param("uniqueCode") String uniqueCode);


    @Query("SELECT i from StorageItem i where i.storage.bom.id= :bomId and i.storage.type= :storageType and i.uniqueCode= :uniqueCode" +
            " and i.storage.onHold = false and i.storage.returned = false and i.storage.isLeafNode = true and i.section.id= :section order by i.storage.createdDate asc")
    List<StorageItem> getItemStorageByBomAndTypeAndUniqueCodeAndSection(@Param("bomId") Integer bomId, @Param("storageType") StorageType storageType,
                                                                        @Param("uniqueCode") String uniqueCode, @Param("section") Integer section);

    @Query("SELECT i from StorageItem i where i.storage.bom.id= :bomId and i.storage.type= :storageType and i.uniqueCode= :uniqueCode" +
            " and i.storage.onHold = false and i.storage.returned = false and i.storage.isLeafNode = true and i.section is null order by i.storage.createdDate asc")
    List<StorageItem> getItemStorageByBomAndTypeAndUniqueCodeAndSectionIsNull(@Param("bomId") Integer bomId, @Param("storageType") StorageType storageType,
                                                                              @Param("uniqueCode") String uniqueCode);

    @Query("SELECT i from StorageItem i where i.storage.bom.id= :bomId and i.uniqueCode= :uniqueCode" +
            " and i.storage.onHold = false and i.storage.returned = false and i.storage.isLeafNode = true and i.section.id= :section order by i.storage.createdDate asc")
    List<StorageItem> getItemStorageByBomAndUniqueCodeAndSection(@Param("bomId") Integer bomId, @Param("uniqueCode") String uniqueCode, @Param("section") Integer section);

    @Query("SELECT i from StorageItem i where i.storage.bom.id= :bomId and i.uniqueCode= :uniqueCode" +
            " and i.storage.onHold = false and i.storage.returned = false and i.storage.isLeafNode = true and i.section is null order by i.storage.createdDate asc")
    List<StorageItem> getItemStorageByBomAndUniqueCodeAndSectionIsNull(@Param("bomId") Integer bomId, @Param("uniqueCode") String uniqueCode);

    List<StorageItem> findByUniqueCode(String uniqueCode);

    @Query("SELECT i from StorageItem i WHERE i.item.id= :item")
    List<StorageItem> findByItem(@Param("item") Integer item);
}
