package com.cassinisys.plm.repo.mes;


import com.cassinisys.plm.model.mes.MESMBOM;
import com.cassinisys.plm.model.mes.MESMBOMType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MESMBOMRepository extends JpaRepository<MESMBOM, Integer>, QueryDslPredicateExecutor<MESMBOM> {

    MESMBOM findByName(String name);

    MESMBOM findByNumber(String number);

    @Query(
            "SELECT i FROM MESMBOM i WHERE i.type.id IN :typeIds"
    )
    Page<MESMBOM> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("select count (i) from MESMBOM i where i.type.id= :typeId")
    Integer getMBOMCountByType(@Param("typeId") Integer typeId);

    List<MESMBOM> findByType(MESMBOMType mesmbomType);
    List<MESMBOM> findByIdIn(List<Integer> ids);

    MESMBOM findByLatestRevision(Integer objectId);

    @Query("SELECT count (i) FROM MESMBOM i WHERE i.type.lifecycle.id= :lifecycle")
    Integer getMBOMsOrderByLifeCycle(@Param("lifecycle") Integer lifecycle);
}
