package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author reddy
 */
@Repository
public interface ObjectTypeAttributeRepository extends
        JpaRepository<ObjectTypeAttribute, Integer>, QueryDslPredicateExecutor<ObjectTypeAttribute> {

    List<ObjectTypeAttribute> findByObjectType(ObjectType targetObjectType);

    List<ObjectTypeAttribute> findByIdIn(Iterable<Integer> ids);

    List<ObjectTypeAttribute> findByObjectTypeAndRequiredTrue(ObjectType objectType);

    List<ObjectTypeAttribute> findByObjectTypeAndRequiredFalse(ObjectType objectType);

    List<ObjectTypeAttribute> findByName(String name);

    ObjectTypeAttribute findByNameAndObjectType(String name, ObjectType objectType);

    ObjectTypeAttribute findByIdAndObjectType(Integer id, ObjectType objectType);

    List<ObjectTypeAttribute> findByLov(Lov lov);

    @Query("select count (i) from ObjectTypeAttribute i where i.lov.id= :lovId")
    Integer getObjectTypeAttributesByLov(@Param("lovId") Integer lovId);

    List<ObjectTypeAttribute> findByDataTypeAndRefTypeAndObjectType(DataType dataType, ObjectType refType, ObjectType objectType);

    @Query("select distinct i.attributeGroup from ObjectTypeAttribute i where i.attributeGroup is not null and i.attributeGroup is not empty order by i.attributeGroup asc")
    List<String> getUniqueAttributeGroups();

    @Query(value = "select i.attribute_group from objecttypeattribute i WHERE lower(cast(i.attribute_group as VARCHAR)) = lower(:attributeGroup)", nativeQuery = true)
    List<String> getAttributeGroupsByName(@Param("attributeGroup") String attributeGroup);

    List<ObjectTypeAttribute> findByAttributeGroup(String attributeGroup);
}
