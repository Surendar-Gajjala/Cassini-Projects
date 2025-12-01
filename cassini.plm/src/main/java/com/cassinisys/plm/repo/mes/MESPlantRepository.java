package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESPlant;
import com.cassinisys.plm.model.mes.MESPlantType;
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
public interface MESPlantRepository extends JpaRepository<MESPlant, Integer>, QueryDslPredicateExecutor<MESPlant> {
    MESPlant findByNameContainingIgnoreCase(String name);
    MESPlant findByName(String name);


    @Query(
            "SELECT i FROM MESPlant i WHERE i.type.id IN :typeIds"
    )
    Page<MESPlant> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query(
            "SELECT i FROM MESPlant i WHERE i.type.id IN :typeIds and i.requiresMaintenance=true"
    )
    List<MESPlant> getByTypeIds(@Param("typeIds") List<Integer> typeId);

    List<MESPlant> findByType(MESPlantType type);

    MESPlant findByNumber(String number);

    @Query("select count (i) from MESPlant i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.address as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.city as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.country as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.postalCode as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.phoneNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.mobileNumber as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.faxAddress as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.email as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.notes as text))) LIKE '%' || :searchText || '%'")
    Integer getPlantCountBySearchQuery(@Param("searchText") String searchText);
}
