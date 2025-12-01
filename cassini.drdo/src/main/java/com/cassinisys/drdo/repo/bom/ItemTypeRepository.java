package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.ItemType;
import com.cassinisys.platform.model.core.AutoNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 03-10-2018.
 */
@Repository
public interface ItemTypeRepository extends JpaRepository<ItemType, Integer>, QueryDslPredicateExecutor<ItemType> {

    @Query("select i from ItemType i where i.parentType is null order by i.name,i.createdDate ASC")
    List<ItemType> findByParentTypeIsNullOrderByNameCreatedDateAsc();

    @Query("select i from ItemType i where i.parentType= :parent order by i.name,i.createdDate ASC")
    List<ItemType> findByParentTypeOrderByNameCreatedDateAsc(@Param("parent") Integer parent);

    List<ItemType> findByIdIn(Iterable<Integer> ids);

    List<ItemType> findByItemNumberSource(AutoNumber autoNumber);

    ItemType findByName(String name);

    ItemType findByTypeCode(String typeCode);

    @Query("SELECT i from ItemType i where i.typeCode is not null and i.typeCode is not empty order by i.name asc")
    List<ItemType> getAllItemTypesWithTypeCodeIsNotNull();

    @Query("SELECT i from ItemType i where i.typeCode is not null and i.hasBom = false order by i.name asc")
    List<ItemType> getAllItemTypesWithTypeCodeIsNotNullAndHasBomIsFalse();

    ItemType findByNameIgnoreCase(String name);

    List<ItemType> findByNameIgnoreCaseAndParentType(String name, Integer parentId);
}
