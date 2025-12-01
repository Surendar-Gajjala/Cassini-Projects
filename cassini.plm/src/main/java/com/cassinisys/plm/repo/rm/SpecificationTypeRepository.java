package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.SpecificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificationTypeRepository extends JpaRepository<SpecificationType, Integer>, QueryDslPredicateExecutor<SpecificationType> {
    @Query("select i from SpecificationType i where i.parentType is null order by i.name,i.createdDate ASC")
    List<SpecificationType> findByParentTypeIsNullOrderByNameCreatedDateAsc();

    List<SpecificationType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

    List<SpecificationType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    SpecificationType findByName(String name);

    List<SpecificationType> findByIdIn(Iterable<Integer> ids);

    @Query("select i from SpecificationType i where i.parentType= :parentType order by i.name,i.createdDate ASC")
    List<SpecificationType> findByParentTypeOrderByNameCreatedDateAsc(@Param("parentType") Integer parentType);

    List<SpecificationType> findByNumberSourceId(Integer autoNumber);
}