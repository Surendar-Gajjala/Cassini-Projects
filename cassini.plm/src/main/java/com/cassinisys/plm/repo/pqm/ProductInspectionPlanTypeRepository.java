package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMProductInspectionPlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface ProductInspectionPlanTypeRepository extends JpaRepository<PQMProductInspectionPlanType, Integer> {
    List<PQMProductInspectionPlanType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PQMProductInspectionPlanType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    List<PQMProductInspectionPlanType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    PQMProductInspectionPlanType findByName(String name);

    PQMProductInspectionPlanType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PQMProductInspectionPlanType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<PQMProductInspectionPlanType> findByIdIn(Iterable<Integer> ids);

    List<PQMProductInspectionPlanType> findByInspectionNumberSourceId(Integer id);

    List<PQMProductInspectionPlanType> findByRevisionSequenceId(Integer id);
}
