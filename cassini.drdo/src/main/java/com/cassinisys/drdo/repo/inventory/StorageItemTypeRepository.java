package com.cassinisys.drdo.repo.inventory;

import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.inventory.StorageItemType;
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
public interface StorageItemTypeRepository extends JpaRepository<StorageItemType, Integer> {

    @Query("SELECT i from StorageItemType i where i.storage.id= :storage and i.itemType.id= :itemType")
    StorageItemType findByStorageAndItemType(@Param("storage") Integer storage, @Param("itemType") Integer itemType);

    List<StorageItemType> findByStorage(Storage storage);

    @Query("SELECT i from StorageItemType i where i.storage.id= :storage order by i.itemType.createdDate ASC")
    List<StorageItemType> getByStorage(@Param("storage") Integer storage);

    @Query("SELECT i from StorageItemType i where i.storage.bom.id= :bomId and i.storage.type= :storageType and i.itemType.id= :typeId" +
            " and i.storage.onHold = false and i.storage.returned = false and i.storage.isLeafNode = true order by i.storage.createdDate desc")
    List<StorageItemType> findItemStorageByBomAndTypeAndTypeId(@Param("bomId") Integer bomId, @Param("storageType") StorageType storageType, @Param("typeId") Integer typeId);
}
