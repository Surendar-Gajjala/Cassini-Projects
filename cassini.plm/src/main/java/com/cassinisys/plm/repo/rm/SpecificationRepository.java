package com.cassinisys.plm.repo.rm;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.rm.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Integer>, QueryDslPredicateExecutor<Specification> {
    Specification findByName(String name);

    Specification findByNameAndObjectType(String name, ObjectType plmObjectType);

    Specification findByNameAndObjectTypeAndLatestTrue(String name, ObjectType plmObjectType);

    List<Specification> findByObjectNumberOrderByCreatedDateDesc(String objectNumber);

    Page<Specification> findByLatestTrueOrderByModifiedDateDesc(Pageable pageable);

    @Query(
            "SELECT i FROM Specification i WHERE i.type.id IN :typeIds"
    )
    Page<Specification> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query(
            "SELECT i FROM Specification i WHERE i.type.id= :typeId"
    )
    List<Specification> getSpecificationsByType(@Param("typeId") Integer typeId);

    @Query(
            "SELECT count (i) FROM Specification i WHERE i.type.lifecycle.id= :lifecycle"
    )
    Integer getSpecByLifeCycle(@Param("lifecycle") Integer lifecycle);
}
