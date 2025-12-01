package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROSparePart;
import com.cassinisys.plm.model.mro.MROSparePartType;
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
public interface MROSparePartRepository extends JpaRepository<MROSparePart, Integer>, QueryDslPredicateExecutor<MROSparePart> {
    MROSparePart findByNameContainingIgnoreCase(String name);

    List<MROSparePart> findByType(MROSparePartType type);

    @Query(
            "SELECT i FROM MROSparePart i WHERE i.type.id IN :typeIds"
    )
    Page<MROSparePart> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    MROSparePart findByNumber(String number);

    @Query("select count (i) from MROSparePart i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getSparePartsCountBySearchQuery(@Param("searchText") String searchText);

}
