package com.cassinisys.is.repo.procm;
/**
 * The Class is for ItemRepository
 **/

import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.model.procm.ISMaterialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialItemRepository extends JpaRepository<ISMaterialItem, Integer>, QueryDslPredicateExecutor<ISMaterialItem> {
    @Query(
            "SELECT i FROM ISMaterialItem i WHERE i.itemType.id= :typeId"
    )
    /**
     * The method used to findByItemTypeId of ISMaterialItem
     **/
    Page<ISMaterialItem> findByItemTypeId(@Param("typeId") Integer typeId, Pageable pageable);

    @Query(
            "SELECT i FROM ISMaterialItem i WHERE i.itemType.id IN :typeIds"
    )
    /**
     * The method used to findByItemTypeIds of ISMaterialItem
     **/
    Page<ISMaterialItem> findByItemTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    /**
     * The method used to findByIdIn of ISMaterialItem
     **/
    List<ISMaterialItem> findByIdIn(Iterable<Integer> ids);

    Page<ISMaterialItem> findByIdIn(Iterable<Integer> ids, Pageable pageable);

    /**
     * The method used to findByItemNumber of ISMaterialItem
     **/
    ISMaterialItem findByItemNumber(String itemNumber);

    ISMaterialItem getByItemNumber(String itemNumber);

    List<ISMaterialItem> findByItemType(ISMaterialType typeId);

    List<ISMaterialItem> findByItemTypeIdIn(Iterable<Integer> typeIds);

    List<ISMaterialItem> findByItemNumberIn(Iterable<String> itemNumbers);

    ISMaterialItem findByItemNameAndItemType(String itemName, ISMaterialType materialType);

    ISMaterialItem findByItemNumberAndItemType(String itemNumber, ISMaterialType materialType);
}
