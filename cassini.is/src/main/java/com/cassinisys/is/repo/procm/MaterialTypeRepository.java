package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISMaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialTypeRepository extends JpaRepository<ISMaterialType, Integer> {

    @Query("SELECT m FROM ISMaterialType m where m.parentType = null ORDER BY lower(m.name)")
    List<ISMaterialType> findByParentTypeIsNullOrderByName();

    @Query("SELECT m FROM ISMaterialType m where m.parentType = :parent ORDER BY lower(m.name)")
    List<ISMaterialType> findByParentTypeOrderByName(@Param("parent") Integer parent);

    ISMaterialType findByName(String itemType);

    List<ISMaterialType> findByIdIn(Iterable<Integer> ids);

    List<ISMaterialType> findByMaterialNumberSourceId(Integer autoNumber);

    List<ISMaterialType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<ISMaterialType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);
}
