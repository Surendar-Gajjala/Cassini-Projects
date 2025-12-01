package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.pgc.PGCDeclarationPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 25-11-2020.
 */
@Repository
public interface PGCDeclarationPartRepository extends JpaRepository<PGCDeclarationPart, Integer>, QueryDslPredicateExecutor<PGCDeclarationPart> {

    List<PGCDeclarationPart> findByDeclaration(Integer id);

    @Query("select i.part.id from PGCDeclarationPart i where i.declaration= :id")
    List<Integer> getPartIdByDeclaration(@Param("id") Integer id);

    @Query("select count(i) from PGCDeclarationPart i where i.declaration= :id")
    Integer getDeclarationPartCount(@Param("id") Integer id);

    void deleteByDeclarationAndPart(Integer id, PLMManufacturerPart part);

    @Query("select i from PGCDeclarationPart i where i.part.id= :part")
    List<PGCDeclarationPart> getDeclarationPartByPart(@Param("part") Integer part);

    PGCDeclarationPart findByDeclarationAndPart(Integer decId, PLMManufacturerPart part);
}
