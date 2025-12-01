package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESInstrument;
import com.cassinisys.plm.model.mes.MESInstrumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface MESInstrumentRepository extends JpaRepository<MESInstrument, Integer>, QueryDslPredicateExecutor<MESInstrument> {

    MESInstrument findByNameContainingIgnoreCase(String name);
    MESInstrument findByName(String name);

    @Query(
            "SELECT i FROM MESInstrument i WHERE i.type.id IN :typeIds"
    )
    Page<MESInstrument> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query(
            "SELECT i FROM MESInstrument i WHERE i.type.id IN :typeIds and i.requiresMaintenance=true"
    )
    List<MESInstrument> getByTypeIds(@Param("typeIds") List<Integer> typeId);

    List<MESInstrument> findByType(MESInstrumentType type);

    MESInstrument findByNumber(String number);

    @Query("select count (i) from MESInstrument i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getInstrumentCountBySearchQuery(@Param("searchText") String searchText);
}
