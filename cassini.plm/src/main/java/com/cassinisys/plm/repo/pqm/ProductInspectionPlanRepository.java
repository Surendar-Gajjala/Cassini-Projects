package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMProductInspectionPlan;
import com.cassinisys.plm.model.pqm.PQMProductInspectionPlanType;
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
public interface ProductInspectionPlanRepository extends JpaRepository<PQMProductInspectionPlan, Integer>, QueryDslPredicateExecutor<PQMProductInspectionPlan> {

    List<PQMProductInspectionPlan> findByPlanType(PQMProductInspectionPlanType planType);

    List<PQMProductInspectionPlan> findByIdIn(Iterable<Integer> ids);

    PQMProductInspectionPlan findByNumber(String number);

    PQMProductInspectionPlan findByName(String number);

    List<PQMProductInspectionPlan> findByProduct(Integer product);

    @Query(
            "SELECT count (i)FROM PQMProductInspectionPlan i WHERE i.planType.lifecycle.id= :lifecycle"
    )
    Integer getPlanByLifeCycle(@Param("lifecycle") Integer lifecycle);

    @Query("select count (i) from PQMProductInspectionPlan i where (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.planType.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%' or i.product in :itemIds")
    Integer getProductInspectionPlansCountBySearchQueryAndItemIds(@Param("searchText") String searchText, @Param("itemIds") Iterable<Integer> itemIds);

    @Query("select count (i) from PQMProductInspectionPlan i where (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.planType.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getProductInspectionPlansCountBySearchQuery(@Param("searchText") String searchText);

    @Query("select i.latestRevision from PQMProductInspectionPlan i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    List<Integer> getProductInspectionPlansCountByName(@Param("searchText") String searchText);

    @Query("select i.latestRevision from PQMProductInspectionPlan i where i.product in :productIds")
    List<Integer> getProductInspectionPlansCountByItemIds(@Param("productIds") Iterable<Integer> productIds);

    @Query("select i.latestRevision from PQMProductInspectionPlan i where i.latestRevision in :ids")
    List<Integer> getIdsByIds(@Param("ids") List<Integer> ids);

    @Query("SELECT count (i) FROM PQMProductInspectionPlan i WHERE i.planType.id= :id")
    Integer getPlanCountByType(@Param("id") Integer id);
}
