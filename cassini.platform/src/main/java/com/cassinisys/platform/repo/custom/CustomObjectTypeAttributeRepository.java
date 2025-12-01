package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomObjectTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomObjectTypeAttributeRepository extends JpaRepository<CustomObjectTypeAttribute, Integer> {
    List<CustomObjectTypeAttribute> findByIdIn(Iterable<Integer> ids);

    List<CustomObjectTypeAttribute> findByCustomObjectTypeOrderBySeqNo(Integer typeId);

    List<CustomObjectTypeAttribute> findByCustomObjectType(Integer typeId);

    CustomObjectTypeAttribute findByCustomObjectTypeAndName(Integer id, String name);

    CustomObjectTypeAttribute findByNameAndObjectType(String name, ObjectType objectType);

}
