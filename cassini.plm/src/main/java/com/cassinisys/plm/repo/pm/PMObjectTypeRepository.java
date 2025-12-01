package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PMObjectType;
import com.cassinisys.plm.model.pm.PMType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PMObjectTypeRepository extends JpaRepository<PMObjectType, Integer>, QueryDslPredicateExecutor<PMObjectType> {

    List<PMObjectType> findByIdIn(Iterable<Integer> ids);

    List<PMObjectType> findByParentIsNullOrderByCreatedDateAsc();

    List<PMObjectType> findByParentIsNullOrderByIdAsc();

    List<PMObjectType> findByTypeAndParentOrderByIdAsc(PMType type, Integer id);

    List<PMObjectType> findByTypeAndParentIsNullOrderByIdAsc(PMType type);

    @Query("select i from PMObjectType i where i.parent is null ORDER BY CASE WHEN i.type = 'PROGRAM' THEN 1" +
            " WHEN i.type = 'PROJECT' THEN 2 WHEN i.type = 'PROJECTPHASEELEMENT' THEN 3 WHEN i.type = 'PROJECTACTIVITY' THEN 4 WHEN i.type = 'PROJECTTASK' THEN 5 END")
    List<PMObjectType> getObjectTypeSpecifiedOrder();

    List<PMObjectType> findByParentOrderByIdAsc(Integer id);

    List<PMObjectType> findByParentOrderByCreatedDateAsc(Integer id);

    PMObjectType findByParentIsNullAndNameEqualsIgnoreCase(String name);

    PMObjectType findByNameEqualsIgnoreCaseAndParent(String name, Integer parentType);

    PMObjectType findByNameAndParentIsNullAndType(String name, PMType type);

    PMObjectType findByTypeAndParentIsNullOrderByCreatedDateAsc(PMType type);

    @Query("select count (i) from PMObjectType i where i.autoNumberSource.id= :autoNumber")
    Integer getPMTypeAutoNumberValueCount(@Param("autoNumber") Integer autoNumber);
}
