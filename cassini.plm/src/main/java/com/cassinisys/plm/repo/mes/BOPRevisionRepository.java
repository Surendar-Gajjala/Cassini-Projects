package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BOPRevisionRepository extends JpaRepository<MESBOPRevision, Integer>, QueryDslPredicateExecutor<MESBOPRevision> {
    List<MESBOPRevision> findByMbomRevisionOrderByIdDesc(Integer mbomRevision);

    @Query("select count (i) from MESBOPRevision i where i.mbomRevision= :mbomRevision")
    Integer getBopCountByMbomRevision(@Param("mbomRevision") Integer mbomRevision);

    @Query("select distinct iR.master from com.cassinisys.plm.model.mes.MESBOPRevision iR,com.cassinisys.plm.model.mes.MESBOP i" +
            " where (iR.released = true and i.latestReleasedRevision is not null and iR.id = i.latestReleasedRevision) and iR.mbomRevision= :mbomRevision")
    List<Integer> getLatestReleasedMasterIdsByMbomRevision(@Param("mbomRevision") Integer mbomRevision);

    List<MESBOPRevision> findByMasterOrderByCreatedDateDesc(Integer master);

    List<MESBOPRevision> findByMasterAndReleasedTrueOrderByCreatedDateDesc(Integer master);

    List<MESBOPRevision> findByMbomRevisionAndReleasedTrueOrderByIdDesc(Integer mbomRevison);

    List<MESBOPRevision> findByMbomRevisionAndReleasedFalseAndRejectedFalseOrderByIdDesc(Integer mbomRevison);
}
