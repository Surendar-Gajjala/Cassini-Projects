package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.pqm.PQMInspectionPlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface InspectionPlanTypeRepository extends JpaRepository<PQMInspectionPlanType, Integer> {
    List<PQMInspectionPlanType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PQMInspectionPlanType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    List<PQMInspectionPlanType> findByIdIn(Iterable<Integer> ids);

    List<PQMInspectionPlanType> findByInspectionNumberSourceId(Integer id);

    List<PQMInspectionPlanType> findByRevisionSequenceId(Integer id);

    PQMInspectionPlanType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PQMInspectionPlanType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    @Query("select count (i) from PQMInspectionPlanType i where i.revisionSequence.id= :lovId")
    Integer getInspectionPlanTypesByLov(@Param("lovId") Integer lovId);

    @Query(value = "SELECT count(ip) FROM pqm_inspection_plan ip JOIN pqm_product_inspection_plan pip on ip.id = pip.id JOIN pqm_inspection_plan_revision ir ON ip.id = ir.plan\n" +
            "  JOIN pqm_product_inspection_plan_type pipt ON pipt.id = pip.plan_type JOIN pqm_inspection_plan_type it ON it.id = pipt.id\n" +
            "  WHERE ir.revision= :lovValue AND it.revision_sequence= :lov", nativeQuery = true)
    Integer getPlanTypeLovValueCount(@Param("lovValue") String lovValue, @Param("lov") Integer lov);

    @Query("select distinct i.lifecycle from PQMInspectionPlanType i")
    List<PLMLifeCycle> getUniqueInspectionTypeLifeCycles();
}
