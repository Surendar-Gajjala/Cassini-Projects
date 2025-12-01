package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.Requirement;
import com.cassinisys.plm.model.rm.SpecElementType;
import com.cassinisys.plm.model.rm.SpecRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecRequirementRepository extends JpaRepository<SpecRequirement, Integer>, QueryDslPredicateExecutor<SpecRequirement> {
    List<SpecRequirement> findByParentOrderByCreatedDateDesc(Integer parent);

    List<SpecRequirement> findByParentOrderByCreatedDateAsc(Integer parent);

    @Query("SELECT i from SpecRequirement i where i.parent= :parent order by i.seqNumber ASC")
    List<SpecRequirement> getRequirementsByParent(@Param("parent") Integer parent);

    List<SpecRequirement> findByParentAndTypeOrderByCreatedDateAsc(Integer parent, SpecElementType type);

    SpecRequirement findByRequirement(Requirement requirement);

    SpecRequirement findByParentAndRequirement(Integer parent, Requirement requirement);

    List<SpecRequirement> findBySpecification(Integer specId);

    SpecRequirement findBySpecificationAndId(Integer specId, Integer id);

    List<SpecRequirement> findBySpecificationAndParentIsNullOrderByCreatedDateAsc(Integer specId);
}
