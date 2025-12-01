package com.cassinisys.is.repo.procm;
/**
 * The Class is for ItemRepository
 **/

import com.cassinisys.is.model.procm.ISMachineItem;
import com.cassinisys.is.model.procm.ISMachineType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineItemRepository extends JpaRepository<ISMachineItem, Integer>, QueryDslPredicateExecutor<ISMachineItem> {
    @Query(
            "SELECT i FROM ISMachineItem i WHERE i.itemType.id= :typeId"
    )
    /**
     * The method used to findByItemTypeId of ISMaterialItem
     **/
    Page<ISMachineItem> findByItemTypeId(@Param("typeId") Integer typeId, Pageable pageable);

    @Query(
            "SELECT i FROM ISMachineItem i WHERE i.itemType.id IN :typeIds"
    )
    /**
     * The method used to findByItemTypeIds of ISMaterialItem
     **/
    Page<ISMachineItem> findByItemTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    /**
     * The method used to findByIdIn of ISMaterialItem
     **/
    List<ISMachineItem> findByIdIn(Iterable<Integer> ids);

    /**
     * The method used to findByItemNumber of ISMaterialItem
     **/
    ISMachineItem findByItemNumber(String itemNumber);

    List<ISMachineItem> findByItemTypeIdIn(List<Integer> typeId);

    List<ISMachineItem> findByItemNumberIn(Iterable<String> itemNumbers);

    ISMachineItem findByItemNameAndItemType(String itemANme, ISMachineType machineType);

    ISMachineItem findByItemNumberAndItemType(String itemNumber, ISMachineType materialType);
}
