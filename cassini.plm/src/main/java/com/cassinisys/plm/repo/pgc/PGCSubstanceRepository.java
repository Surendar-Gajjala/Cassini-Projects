package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCSubstance;
import com.cassinisys.plm.model.pgc.PGCSubstanceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 25-11-2020.
 */
@Repository
public interface PGCSubstanceRepository extends JpaRepository<PGCSubstance, Integer>, QueryDslPredicateExecutor<PGCSubstance> {

    List<PGCSubstance> findByIdIn(Iterable<Integer> ids);

    List<PGCSubstance> findByType(PGCSubstanceType type);

    PGCSubstance findByName(String name);

    PGCSubstance findByNumber(String number);

    @Query(
            "SELECT i FROM PGCSubstance i WHERE i.type.id IN :typeIds"
    )
    Page<PGCSubstance> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select count (i) from PGCSubstance i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.casNumber as text))) LIKE '%' || :searchText || '%'")
    Integer getSubstanceCountBySearchQuery(@Param("searchText") String searchText);

}
