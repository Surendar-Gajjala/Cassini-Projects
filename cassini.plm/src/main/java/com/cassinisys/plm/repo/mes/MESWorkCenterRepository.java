package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESWorkCenter;
import com.cassinisys.plm.model.mes.MESWorkCenterType;
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
public interface MESWorkCenterRepository extends JpaRepository<MESWorkCenter, Integer>, QueryDslPredicateExecutor<MESWorkCenter> {

    List<MESWorkCenter> findByIdIn(Iterable<Integer> ids);

    @Query(
            "SELECT i FROM MESWorkCenter i WHERE i.type.id IN :typeIds"
    )
    Page<MESWorkCenter> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query(
            "SELECT i FROM MESWorkCenter i WHERE i.type.id IN :typeIds and i.requiresMaintenance=true"
    )
    List<MESWorkCenter> getByTypeIds(@Param("typeIds") List<Integer> typeId);

    List<MESWorkCenter> findByType(MESWorkCenterType type);

    List<MESWorkCenter> findByPlant(Integer plantId);

    List<MESWorkCenter> findByAssemblyLine(Integer assemblyLine);

    MESWorkCenter findByNumber(String number);

    @Query("select count (i) from MESWorkCenter i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getWorkCenterCountBySearchQuery(@Param("searchText") String searchText);
}
