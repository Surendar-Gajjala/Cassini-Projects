package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROMaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROMaintenancePlanRepository extends JpaRepository<MROMaintenancePlan, Integer>, QueryDslPredicateExecutor<MROMaintenancePlan> {
    MROMaintenancePlan findByNameContainingIgnoreCase(String name);

    List<MROMaintenancePlan> findByAsset(Integer asset);
    MROMaintenancePlan findByNumber(String number);

    @Query("SELECT count (i) FROM MROMaintenancePlan i")
    Integer getMaintenancePlanCount();

    @Query("select count (i) from MROMaintenancePlan i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getMaintenancePlansCountBySearchQuery(@Param("searchText") String searchText);
}
