package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMMaterialInspectionPlan;
import com.cassinisys.plm.model.pqm.PQMMaterialInspectionPlanType;
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
public interface MaterialInspectionPlanRepository extends JpaRepository<PQMMaterialInspectionPlan, Integer>, QueryDslPredicateExecutor<PQMMaterialInspectionPlan> {

    List<PQMMaterialInspectionPlan> findByPlanType(PQMMaterialInspectionPlanType planType);

    List<PQMMaterialInspectionPlan> findByIdIn(Iterable<Integer> ids);

    PQMMaterialInspectionPlan findByNumber(String number);

    PQMMaterialInspectionPlan findByName(String number);

    List<PQMMaterialInspectionPlan> findByMaterialId(Integer product);

    @Query(
            "SELECT count (i) FROM PQMMaterialInspectionPlan i WHERE i.planType.lifecycle.id= :lifecycle"
    )
    Integer getPlanByLifeCycle(@Param("lifecycle") Integer lifecycle);

    @Query("select count (i) from PQMMaterialInspectionPlan i where (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.planType.name as text))) LIKE '%' || :searchText || '%'")
    Integer getMaterialInspectionsCountBySearchQuery(@Param("searchText") String searchText);

    @Query("select count (i) from PQMMaterialInspectionPlan i where (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.planType.name as text))) LIKE '%' || :searchText || '%' or i.material.id in :partIds")
    Integer getMaterialInspectionsCountBySearchQueryAndPartIds(@Param("searchText") String searchText, @Param("partIds") Iterable<Integer> partIds);

    @Query("select i.latestRevision from PQMMaterialInspectionPlan i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    List<Integer> getMaterialInspectionPlansByName(@Param("searchText") String searchText);

    @Query("select i.latestRevision from PQMMaterialInspectionPlan i where i.material.id in :partIds")
    List<Integer> getMaterialInspectionPlansByPartIds(@Param("partIds") Iterable<Integer> partIds);

    @Query("select i.latestRevision from PQMMaterialInspectionPlan i where i.latestRevision in :ids")
    List<Integer> getIdsByIds(@Param("ids") List<Integer> ids);

}
