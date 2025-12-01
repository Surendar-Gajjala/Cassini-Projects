package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROWorkOrder;
import com.cassinisys.plm.model.mro.MROWorkOrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface MROWorkOrderRepository extends JpaRepository<MROWorkOrder, Integer>, QueryDslPredicateExecutor<MROWorkOrder> {

    MROWorkOrder findByName(String name);

    List<MROWorkOrder> findByType(MROWorkOrderType type);

    @Query(
            "SELECT i FROM MROWorkOrder i WHERE i.type.id IN :typeIds"
    )
    Page<MROWorkOrder> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<MROWorkOrder> findByRequest(Integer request);

    List<MROWorkOrder> findByPlan(Integer plan);

    @Query("select i.request from MROWorkOrder i where i.request is not null")
    List<Integer> getWorkRequestIdsFromWorkOrder();

    List<MROWorkOrder> findByAsset(Integer assetId);

    MROWorkOrder findByNumber(String number);

    @Query("SELECT count (i) FROM MROWorkOrder i")
    Integer getWorkOrdersCount();

    @Query("select count (i) from MROWorkOrder i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getWorkOrderCountBySearchQuery(@Param("searchText") String searchText);
}
