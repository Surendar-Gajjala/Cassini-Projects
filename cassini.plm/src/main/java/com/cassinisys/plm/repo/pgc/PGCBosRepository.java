package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCBos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 25-11-2020.
 */
@Repository
public interface PGCBosRepository extends JpaRepository<PGCBos, Integer> {

    @Query("select distinct i.part from PGCBos i")
    List<Integer> getUniquePartIds();

}
