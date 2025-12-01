package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCSpecificationSubstance;
import com.cassinisys.plm.model.pgc.PGCSubstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCSpecificationSubstanceRepository extends JpaRepository<PGCSpecificationSubstance, Integer> {

    List<PGCSpecificationSubstance> findBySpecification(Integer id);

    void deleteBySpecificationAndSubstance(Integer specId, PGCSubstance substance);

    List<PGCSpecificationSubstance> findBySubstance(PGCSubstance substance);

    PGCSpecificationSubstance findBySpecificationAndSubstance(Integer id, PGCSubstance substance);

    @Query("select count (i) from PGCSpecificationSubstance i where i.specification= :specId")
    Integer getSpecificationSubstacesCount(@Param("specId") Integer specId);

    @Query("select i.substance.id from PGCSpecificationSubstance i where i.specification in :specIds")
    List<Integer> getSubstanceIdsBySpecificationIds(@Param("specIds") Iterable<Integer> specIds);

    @Query("select i from PGCSpecificationSubstance i where i.substance.id= :substanceId and i.specification in :specIds")
    List<PGCSpecificationSubstance> getSubstancesBySpecificationIds(@Param("substanceId") Integer substanceId, @Param("specIds") Iterable<Integer> specIds);
}
