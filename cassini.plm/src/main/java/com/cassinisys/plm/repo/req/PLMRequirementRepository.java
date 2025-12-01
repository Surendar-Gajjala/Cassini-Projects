package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.req.PLMRequirement;
import com.cassinisys.plm.model.req.PLMRequirementDocumentRevision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface PLMRequirementRepository extends JpaRepository<PLMRequirement, Integer>, QueryDslPredicateExecutor<PLMRequirement> {
    PLMRequirement findByNumber(String number);

    List<PLMRequirement> findByIdIn(Iterable<Integer> ids);

    @Query("select o.name from PLMRequirementObjectType o, PLMRequirement r where o.id = r.type GROUP BY (o.id)")
    List<String> getRequirementsTypes();

    @Query("select count(r.type) from PLMRequirementObjectType o, PLMRequirement r where o.id = r.type GROUP BY (o.id)")
    List<Long> getRequirementsCounts();

    @Query(
            "SELECT i FROM PLMRequirement i WHERE i.type.id IN :typeIds"
    )
    Page<PLMRequirement> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select i.id from PLMRequirement i where i.type.id= :id")
    List<Integer> getRequirementIdsByType(@Param("id") Integer id);
}
