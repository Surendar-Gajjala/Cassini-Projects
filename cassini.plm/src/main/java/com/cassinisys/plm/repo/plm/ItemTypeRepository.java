package com.cassinisys.plm.repo.plm;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.plm.ItemClass;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemTypeRepository extends JpaRepository<PLMItemType, Integer> {
    List<PLMItemType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PLMItemType> findByParentTypeIsNullOrderByIdAsc();

    List<PLMItemType> findByItemClassAndParentTypeIsNullOrderByCreatedDateAsc(ItemClass itemClass);

    List<PLMItemType> findByItemClassAndParentTypeIsNullOrderByIdAsc(ItemClass itemClass);

    @Query("select i from PLMItemType i where i.parentType is null order by i.name,i.createdDate ASC")
    List<PLMItemType> findByParentTypeIsNullOrderByNameCreatedDateAsc();

    List<PLMItemType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    List<PLMItemType> findByParentTypeOrderByIdAsc(Integer parent);

    @Query("select i from PLMItemType i where i.parentType= :parent order by i.name,i.createdDate ASC")
    List<PLMItemType> findByParentTypeOrderByNameCreatedDateAsc(@Param("parent") Integer parent);

    PLMItemType findByName(String name);

    PLMItemType findByNameEqualsIgnoreCase(String name);

    List<PLMItemType> findByLifecycle(PLMLifeCycle lc);

    List<PLMItemType> findByIdIn(Iterable<Integer> ids);

    List<PLMItemType> findByItemNumberSourceId(Integer autoNumber);

    List<PLMItemType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

    List<PLMItemType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<PLMItemType> findByRevisionSequence(Lov lovId);

    PLMItemType findByParentTypeIsNullAndItemClass(ItemClass itemClass);

    @Query("select count (i) from PLMItemType i where i.revisionSequence.id= :lovId")
    Integer getItemTypeCountByRevisionSequence(@Param("lovId") Integer lovId);

    List<PLMItemType> findByItemClass(ItemClass itemClass);

    @Query("select distinct i.lifecycle from PLMItemType i where i.itemClass= :itemClass")
    List<PLMLifeCycle> getUniqueLifeCyclesByItemClass(@Param("itemClass") ItemClass itemClass);

    @Query("select distinct i.lifecycle from PLMItemType i")
    List<PLMLifeCycle> getUniqueItemTypeLifeCycles();

    @Query("select count (i) from PLMItemType i where i.itemNumberSource.id= :autoNumber")
    Integer getItemTypeAutoNumberValueCount(@Param("autoNumber") Integer autoNumber);
}
