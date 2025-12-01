package com.cassinisys.plm.repo.mes;


import com.cassinisys.plm.model.mes.MESBOP;
import com.cassinisys.plm.model.mes.MESBOPType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MESBOPRepository extends JpaRepository<MESBOP, Integer>, QueryDslPredicateExecutor<MESBOP> {

    MESBOP findByName(String name);

    MESBOP findByNumber(String number);

    @Query(
            "SELECT i FROM MESBOP i WHERE i.type.id IN :typeIds"
    )
    Page<MESBOP> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select count (i) from MESBOP i where i.type.id= :typeId")
    Integer getBOPCountByType(@Param("typeId") Integer typeId);

    List<MESBOP> findByType(MESBOPType mesbopType);

    List<MESBOP> findByIdIn(List<Integer> ids);
}
