package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomObjectBom;
import com.cassinisys.platform.model.custom.CustomObjectRelated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomObjectRelatedRepository extends JpaRepository<CustomObjectRelated, Integer>, QueryDslPredicateExecutor<CustomObjectRelated> {

    List<CustomObjectRelated> findByParent(Integer id);

    @Query("select count (i) from CustomObjectRelated i where i.parent= :parent")
    Integer getRelatedCustomObjectCountByParent(@Param("parent") Integer parent);

    @Query("select i from CustomObjectRelated i where i.parent= :parent")
    List<CustomObjectRelated> findByParentOrderBySequenceAsc(@Param("parent") Integer parent);
}
