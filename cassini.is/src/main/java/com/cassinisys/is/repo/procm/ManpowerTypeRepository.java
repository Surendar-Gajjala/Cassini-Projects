package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISManpowerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManpowerTypeRepository extends JpaRepository<ISManpowerType, Integer> {
    @Query("SELECT m FROM ISManpowerType m where m.parentType = null ORDER BY lower(m.name)")
    List<ISManpowerType> findByParentTypeIsNullOrderByName();

    @Query("SELECT m FROM ISManpowerType m where m.parentType = :parent ORDER BY lower(m.name)")
    List<ISManpowerType> findByParentTypeOrderByName(@Param("parent") Integer parent);

    List<ISManpowerType> findByName(String itemType);

    List<ISManpowerType> findByIdIn(Iterable<Integer> ids);

    List<ISManpowerType> findByManpowerNumberSourceId(Integer autoNumber);

    List<ISManpowerType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<ISManpowerType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);
}
