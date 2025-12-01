package com.cassinisys.plm.repo.rm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.plm.model.rm.Requirement;
import com.cassinisys.plm.model.rm.RequirementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Integer>, QueryDslPredicateExecutor<Requirement> {

    List<Requirement> findBySpecificationAndLatestTrue(Integer specification);

    Page<Requirement> findByAssignedTo(Person person, Pageable pageable);

    List<Requirement> findByObjectNumberOrderByCreatedDateDesc(String objectNumber);

    Requirement findBySpecification(Integer specId);

    Page<Requirement> findByLatestTrue(Pageable pageable);

    @Query("SELECT MIN(requirement.plannedFinishDate) FROM Requirement requirement ")
    Date getMinDate();

    Requirement findBySpecificationAndId(Integer specId, Integer id);

    @Query(
            "SELECT i FROM Requirement i WHERE i.type.id IN :typeIds"
    )
    Page<Requirement> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query(
            "SELECT DISTINCT i.requirement.version FROM SpecRequirement i WHERE i.specification= :specId AND i.requirement.latest = true"
    )
    List<Requirement> getVersionsBySpecification(@Param("specId") Integer specId);

    @Query(
            "SELECT i FROM Requirement i WHERE i.type.id= :typeId"
    )
    List<Requirement> getRequirementsByType(@Param("typeId") Integer typeId);

    List<Requirement> findBySpecificationAndObjectNumberOrderByCreatedDateAsc(Integer specId, String objectNumber);

    @Query("select count(i) from Requirement i where i.status =:status")
    Integer getByStatusTypeRequriments(@Param("status") RequirementStatus status);

    @Query("select o.name from com.cassinisys.plm.model.rm.RmObjectType o, com.cassinisys.plm.model.rm.Requirement r where o.id = r.type GROUP BY (o.id)")
    List<String> getRequirementsTypes();

    @Query("select count(r.type) from com.cassinisys.plm.model.rm.RmObjectType o, com.cassinisys.plm.model.rm.Requirement r where o.id = r.type GROUP BY (o.id)")
    List<Long> getRequirementsCounts();
}
