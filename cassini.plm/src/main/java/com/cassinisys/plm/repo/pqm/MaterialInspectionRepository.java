package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMMaterialInspection;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface MaterialInspectionRepository extends JpaRepository<PQMMaterialInspection, Integer>, QueryDslPredicateExecutor<PQMMaterialInspection> {

    List<PQMMaterialInspection> findByStatusType(WorkflowStatusType statusType);

    List<PQMMaterialInspection> findByMaterial(Integer item);

    PQMMaterialInspection findByInspectionNumber(String number);

    List<PQMMaterialInspection> findByIdIn(Iterable<Integer> ids);

    List<PQMMaterialInspection> findByInspectionPlan(Integer id);

    @Query(value = "select i.material,count(i.material) from pqm_material_inspection i JOIN pqm_inspection j ON i.id = j.id where j.status_type = 'REJECTED' group by i.material order by count (i.material) desc LIMIT 10", nativeQuery = true)
    List<Object[]> getInspectionFailedMaterials();

    @Query("select count (i) from PQMItemInspection i where (LOWER(CAST(i.inspectionNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.deviationSummary as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.notes as text))) LIKE '%' || :searchText || '%'" +
            " or i.inspectionPlan in :inspectionPlanIds")
    Integer getMaterialInspectionsCountBySearchQueryAndItemIds(@Param("searchText") String searchText, @Param("inspectionPlanIds") Iterable<Integer> inspectionPlanIds);

    @Query("select count (i) from PQMItemInspection i where (LOWER(CAST(i.inspectionNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.deviationSummary as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.notes as text))) LIKE '%' || :searchText || '%'")
    Integer getMaterialInspectionsCountBySearchQuery(@Param("searchText") String searchText);

}
