package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROWorkRequest;
import com.cassinisys.plm.model.mro.MROWorkRequestType;
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
public interface MROWorkRequestRepository extends JpaRepository<MROWorkRequest, Integer>, QueryDslPredicateExecutor<MROWorkRequest> {

    List<MROWorkRequest> findByIdIn(Iterable<Integer> ids);

    List<MROWorkRequest> findByIdNotIn(Iterable<Integer> ids);

    List<MROWorkRequest> findByType(MROWorkRequestType type);

    MROWorkRequest findByName(String name);

    MROWorkRequest findByNumber(String number);

    @Query(
            "SELECT i FROM MROWorkRequest i WHERE i.type.id IN :typeIds"
    )
    Page<MROWorkRequest> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<MROWorkRequest> findByAsset(Integer asset);

    @Query("SELECT count (i) FROM MROWorkRequest i")
    Integer getWorkRequestsCount();

    @Query("select count (i) from MROWorkRequest i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getWorkRequestCountBySearchQuery(@Param("searchText") String searchText);
}
