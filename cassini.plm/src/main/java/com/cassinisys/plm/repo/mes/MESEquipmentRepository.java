package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESEquipment;
import com.cassinisys.plm.model.mes.MESEquipmentType;
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
public interface MESEquipmentRepository extends JpaRepository<MESEquipment, Integer>, QueryDslPredicateExecutor<MESEquipment> {
    MESEquipment findByNameContainingIgnoreCase(String name);
    MESEquipment findByName(String name);

    @Query(
            "SELECT i FROM MESEquipment i WHERE i.type.id IN :typeIds"
    )
    Page<MESEquipment> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query(
            "SELECT i FROM MESEquipment i WHERE i.type.id IN :typeIds and i.requiresMaintenance=true"
    )
    List<MESEquipment> getByTypeIds(@Param("typeIds") List<Integer> typeId);

    List<MESEquipment> findByType(MESEquipmentType type);

    MESEquipment findByNumber(String number);

    @Query("select count (i) from MESEquipment i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getEquipemntCountBySearchQuery(@Param("searchText") String searchText);
}
