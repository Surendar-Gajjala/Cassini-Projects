package com.cassinisys.is.repo.procm;
/**
 * The Class is for ItemRepository
 **/

import com.cassinisys.is.model.procm.ISManpowerItem;
import com.cassinisys.platform.model.common.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManpowerItemRepository extends JpaRepository<ISManpowerItem, Integer>, QueryDslPredicateExecutor<ISManpowerItem> {
    @Query(
            "SELECT i FROM ISManpowerItem i WHERE i.itemType.id= :typeId"
    )
    /**
     * The method used to findByItemTypeId of ISMaterialItem
     **/
    Page<ISManpowerItem> findByItemTypeId(@Param("typeId") Integer typeId, Pageable pageable);

    @Query(
            "SELECT i FROM ISManpowerItem i WHERE i.itemType.id IN :typeIds"
    )
    /**
     * The method used to findByItemTypeIds of ISMaterialItem
     **/
    Page<ISManpowerItem> findByItemTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    /**
     * The method used to findByIdIn of ISMaterialItem
     **/
    List<ISManpowerItem> findByIdIn(Iterable<Integer> ids);

    /**
     * The method used to findByItemNumber of ISMaterialItem
     **/
    ISManpowerItem findByItemNumber(String itemNumber);

    List<ISManpowerItem> findByItemTypeIdIn(List<Integer> typeId);

    ISManpowerItem findByPerson(Person person);

    List<ISManpowerItem> findByItemTypeId(Integer itemType);
}
