package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCBosItem;
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
public interface PGCBosItemRepository extends JpaRepository<PGCBosItem, Integer>, QueryDslPredicateExecutor<PGCBosItem> {

    @Query("select i.substance from PGCBosItem i where i.bos= :bos")
    List<Integer> getSubstanceIdsByBOS(@Param("bos") Integer bos);

    List<PGCBosItem> findByBos(Integer bos);
}
