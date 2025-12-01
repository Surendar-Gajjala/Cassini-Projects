package com.cassinisys.drdo.repo.inventory;

import com.cassinisys.drdo.model.bom.Bom;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.inventory.StorageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subra on 07-10-2018.
 */
@Repository
public interface StorageRepository extends JpaRepository<Storage, Integer> {

    List<Storage> findByParentIsNullOrderByCreatedDateAsc();

    List<Storage> findByParentIsNullAndOnHoldFalseAndReturnedFalseOrderByCreatedDateAsc();

    List<Storage> findByParentIsNullAndReturnedTrueOrderByCreatedDateAsc();

    List<Storage> findByParentIsNullAndOnHoldTrueOrderByCreatedDateAsc();

    List<Storage> findByParentOrderByCreatedDateAsc(Integer parent);

    List<Storage> findByParentAndOnHoldFalseAndReturnedFalseOrderByCreatedDateAsc(Integer parent);

    List<Storage> findByParentAndReturnedTrueOrderByCreatedDateAsc(Integer parent);

    List<Storage> findByParentAndOnHoldTrueOrderByCreatedDateAsc(Integer parent);

    Storage findByNameAndType(String name, StorageType type);

    List<Storage> findByName(String name);

    Storage findByNameAndTypeAndParentIsNull(String name, StorageType type);

    Storage findByParentAndNameAndType(Integer parent, String name, StorageType type);

    List<Storage> findByBomAndOnHoldTrueOrderByCreatedDateAsc(Bom bom);

    List<Storage> findByBomAndReturnedTrueOrderByCreatedDateAsc(Bom bom);

    List<Storage> findByBomAndOnHoldTrueAndReturnedTrueOrderByCreatedDateAsc(Bom bom);

    Storage findByNameAndTypeAndBomAndReturnedTrue(String name, StorageType type, Bom bom);

    Storage findByNameAndTypeAndBomAndOnHoldTrue(String name, StorageType type, Bom bom);
}
