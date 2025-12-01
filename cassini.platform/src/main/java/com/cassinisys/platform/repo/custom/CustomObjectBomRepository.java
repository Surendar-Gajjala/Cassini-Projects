package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectBom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 20-07-2021.
 */
@Repository
public interface CustomObjectBomRepository extends JpaRepository<CustomObjectBom, Integer>, QueryDslPredicateExecutor<CustomObjectBom> {

    @Query("select count (i) from CustomObjectBom i where i.parent.id= :parent")
    Integer getCustomObjectBomCountByParent(@Param("parent") Integer parent);

    @Query("select i from CustomObjectBom i where i.parent.id in :parentIds")
    List<CustomObjectBom> getCustomObjectBomCountByParentIdsIn(@Param("parentIds") List<Integer> parentIds);

    @Query("select i from CustomObjectBom i where i.parent.id= :parent order by i.sequence asc")
    List<CustomObjectBom> findByParentOrderBySequenceAsc(@Param("parent") Integer parent);

    @Query("select i from CustomObjectBom i where i.child.id= :child")
    List<CustomObjectBom> findByChildOrderBySequenceAsc(@Param("child") Integer child);

    CustomObjectBom findByParentAndChild(Integer parent, Integer child);

    List<CustomObjectBom> findByChild(CustomObject customObject);

    @Query("select count (i) from CustomObjectBom i where i.child.id= :child")
    Integer getObjectsCountByParent(@Param("child") Integer child);
}
