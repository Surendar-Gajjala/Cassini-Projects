package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMachine;
import com.cassinisys.plm.model.mes.MESMachineType;
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
public interface MESMachineRepository extends JpaRepository<MESMachine, Integer>, QueryDslPredicateExecutor<MESMachine> {
    MESMachine findByNameContainingIgnoreCase(String name);
    MESMachine findByName(String name);

    @Query(
            "SELECT i FROM MESMachine i WHERE i.type.id IN :typeIds"
    )
    Page<MESMachine> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query(
            "SELECT i FROM MESMachine i WHERE i.type.id IN :typeIds and i.requiresMaintenance=true"
    )
    List<MESMachine> getByTypeIds(@Param("typeIds") List<Integer> typeId);

    List<MESMachine> findByType(MESMachineType type);

    List<MESMachine> findByWorkCenter(Integer workCenter);

    MESMachine findByNumber(String number);

    @Query("select count (i) from MESMachine i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getMachineCountBySearchQuery(@Param("searchText") String searchText);
}
