package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RequirementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementTypeRepository extends JpaRepository<RequirementType, Integer>, QueryDslPredicateExecutor<RequirementType> {
    @Query("select i from RequirementType i where i.parentType is null order by i.name,i.createdDate ASC")
    List<RequirementType> findByParentTypeIsNullOrderByNameCreatedDateAsc();

    List<RequirementType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

    List<RequirementType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    RequirementType findByName(String name);

    List<RequirementType> findByIdIn(Iterable<Integer> ids);

    @Query("select i from RequirementType i where i.parentType= :parentType order by i.name,i.createdDate ASC")
    List<RequirementType> findByParentTypeOrderByNameCreatedDateAsc(@Param("parentType") Integer parentType);

    List<RequirementType> findByNumberSourceId(Integer autoNumber);

}