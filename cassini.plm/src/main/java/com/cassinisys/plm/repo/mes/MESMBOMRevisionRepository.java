package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMBOMRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MESMBOMRevisionRepository extends JpaRepository<MESMBOMRevision, Integer>, QueryDslPredicateExecutor<MESMBOMRevision> {
    MESMBOMRevision findByItemRevision(Integer item);

    @Query("select i.id from com.cassinisys.plm.model.mes.MESMBOMRevision i, com.cassinisys.plm.model.mes.MESMBOM m where i.master = m.id and m.latestRevision = i.id and i.released = false")
    List<Integer> getInitialRevisionIds();

    @Query("select i.id from com.cassinisys.plm.model.mes.MESMBOMRevision i, com.cassinisys.plm.model.mes.MESMBOM m where i.master = m.id and m.latestRevision = i.id and i.released = true")
    List<Integer> getLatestRevisionIds();

    @Query("select m.latestReleasedRevision from com.cassinisys.plm.model.mes.MESMBOMRevision i, com.cassinisys.plm.model.mes.MESMBOM m " +
            "where i.master = m.id and m.latestRevision = i.id and i.rejected = true and m.latestReleasedRevision is not null")
    List<Integer> getLatestReleasedRevisionIds();

    @Query("select distinct iR.master from com.cassinisys.plm.model.mes.MESMBOMRevision iR,com.cassinisys.plm.model.mes.MESMBOM i" +
    " where (iR.released = true and i.latestReleasedRevision is not null and iR.id = i.latestReleasedRevision)")
    List<Integer> getLatestReleasedMasterIds();

    List<MESMBOMRevision> findByMasterOrderByCreatedDateDesc(Integer master);
    List<MESMBOMRevision> findByMasterAndReleasedTrueOrderByCreatedDateDesc(Integer master);

    @Query("select i.id from MESMBOMRevision i where i.master= :master order by i.id desc")
    List<Integer> getRevisionIdsByMaster(@Param("master") Integer master);
}
