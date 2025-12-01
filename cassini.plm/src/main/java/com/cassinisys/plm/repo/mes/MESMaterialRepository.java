package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMaterial;
import com.cassinisys.plm.model.mes.MESMaterialType;
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
public interface MESMaterialRepository extends JpaRepository<MESMaterial, Integer>, QueryDslPredicateExecutor<MESMaterial> {

    List<MESMaterial> findByIdIn(Iterable<Integer> ids);

    List<MESMaterial> findByType(MESMaterialType type);

    MESMaterial findByNameContainingIgnoreCase(String name);
    MESMaterial findByName(String name);

    MESMaterial findByNumber(String number);

    @Query(
            "SELECT i FROM MESMaterial i WHERE i.type.id IN :typeIds"
    )
    Page<MESMaterial> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select count (i) from MESMaterial i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getMaterialCountBySearchQuery(@Param("searchText") String searchText);
}
