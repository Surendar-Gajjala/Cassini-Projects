package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESAssemblyLine;
import com.cassinisys.plm.model.mes.MESAssemblyLineType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 09-02-2021.
 */
@Repository
public interface MESAssemblyLineRepository extends JpaRepository<MESAssemblyLine, Integer>, QueryDslPredicateExecutor<MESAssemblyLine> {

    List<MESAssemblyLine> findByIdIn(Iterable<Integer> ids);

    MESAssemblyLine findByNameContainingIgnoreCase(String name);

    @Query(
            "SELECT i FROM MESAssemblyLine i WHERE i.type.id IN :typeIds"
    )
    Page<MESAssemblyLine> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<MESAssemblyLine> findByType(MESAssemblyLineType type);

    MESAssemblyLine findByNumber(String number);

    MESAssemblyLine findByPlantAndName(Integer plant, String name);

    MESAssemblyLine findByName(String name);

    @Query("select count (i) from MESAssemblyLine i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getAssemblyLineCountBySearchQuery(@Param("searchText") String searchText);
}
