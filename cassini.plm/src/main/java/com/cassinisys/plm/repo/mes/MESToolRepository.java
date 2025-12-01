package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESTool;
import com.cassinisys.plm.model.mes.MESToolType;
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
public interface MESToolRepository extends JpaRepository<MESTool, Integer>, QueryDslPredicateExecutor<MESTool> {
    MESTool findByNameContainingIgnoreCase(String name);
    MESTool findByName(String name);

    @Query(
            "SELECT i FROM MESTool i WHERE i.type.id IN :typeIds"
    )
    Page<MESTool> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query(
            "SELECT i FROM MESTool i WHERE i.type.id IN :typeIds and i.requiresMaintenance=true"
    )
    List<MESTool> getByTypeIds(@Param("typeIds") List<Integer> typeId);

    List<MESTool> findByType(MESToolType type);

    MESTool findByNumber(String number);

    @Query("select count (i) from MESTool i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getToolCountBySearchQuery(@Param("searchText") String searchText);
}
