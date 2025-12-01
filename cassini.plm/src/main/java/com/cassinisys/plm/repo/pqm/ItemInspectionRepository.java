package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMItemInspection;
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
public interface ItemInspectionRepository extends JpaRepository<PQMItemInspection, Integer>, QueryDslPredicateExecutor<PQMItemInspection> {

    List<PQMItemInspection> findByItemAndStatusType(Integer item, WorkflowStatusType statusType);

    List<PQMItemInspection> findByItem(Integer item);

    @Query("select count(i) from PQMItemInspection i where i.item= :item")
    Integer getItemInspectionCount(@Param("item") Integer item);

    PQMItemInspection findByInspectionNumber(String number);

    List<PQMItemInspection> findByIdIn(Iterable<Integer> ids);

    List<PQMItemInspection> findByInspectionPlan(Integer id);

    @Query(value = "select i.item,count(i.item) from pqm_item_inspection i JOIN pqm_inspection j ON i.id = j.id where j.status_type = 'REJECTED' group by i.item order by count (i.item) desc LIMIT 10", nativeQuery = true)
    List<Object[]> getInspectionFailedProducts();

    @Query("select count (i) from PQMItemInspection i where (LOWER(CAST(i.inspectionNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.deviationSummary as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.notes as text))) LIKE '%' || :searchText || '%'" +
            " or i.inspectionPlan in :inspectionPlanIds")
    Integer getItemInspectionsCountBySearchQueryAndItemIds(@Param("searchText") String searchText, @Param("inspectionPlanIds") Iterable<Integer> inspectionPlanIds);

    @Query("select count (i) from PQMItemInspection i where (LOWER(CAST(i.inspectionNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.deviationSummary as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.notes as text))) LIKE '%' || :searchText || '%'")
    Integer getItemInspectionsCountBySearchQuery(@Param("searchText") String searchText);

}
