package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCSubstanceGroup;
import com.cassinisys.plm.model.pgc.PGCSubstanceGroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCSubstanceGroupRepository extends JpaRepository<PGCSubstanceGroup, Integer> {

    List<PGCSubstanceGroup> findByType(PGCSubstanceGroupType type);
}
