package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMMaterialInspectionPlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface MaterialInspectionPlanTypeRepository extends JpaRepository<PQMMaterialInspectionPlanType, Integer> {
    List<PQMMaterialInspectionPlanType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PQMMaterialInspectionPlanType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    List<PQMMaterialInspectionPlanType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    List<PQMMaterialInspectionPlanType> findByIdIn(Iterable<Integer> ids);

    List<PQMMaterialInspectionPlanType> findByInspectionNumberSourceId(Integer id);

    List<PQMMaterialInspectionPlanType> findByRevisionSequenceId(Integer id);

    PQMMaterialInspectionPlanType findByName(String name);
}
