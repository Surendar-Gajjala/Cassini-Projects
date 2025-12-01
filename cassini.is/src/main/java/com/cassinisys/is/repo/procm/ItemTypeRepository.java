package com.cassinisys.is.repo.procm;
/**
 * The Class is for ItemTypeRepository
 **/

import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.ISItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemTypeRepository extends JpaRepository<ISItemType, Integer> {
    /**
     * The method used to findByIdIn of ISItemType
     **/
    List<ISItemType> findByIdIn(Iterable<Integer> var1);

    /**
     * The method used to findByParentTypeIsNullOrderByCreatedDateAsc of ISItemType
     **/
    List<ISItemType> findByParentTypeIsNullOrderByCreatedDateAsc();

    /**
     * The method used to findByParentTypeOrderByCreatedDateAsc of ISItemType
     **/
    List<ISItemType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    /**
     * The method used to findByResourceType of ISItemType
     **/
    List<ISItemType> findByResourceType(ResourceType resourceType);

}
