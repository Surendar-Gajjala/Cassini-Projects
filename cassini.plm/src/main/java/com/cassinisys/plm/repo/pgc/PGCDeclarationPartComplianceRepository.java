package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCDeclarationPartCompliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 25-11-2020.
 */
@Repository
public interface PGCDeclarationPartComplianceRepository extends JpaRepository<PGCDeclarationPartCompliance, Integer> {

    List<PGCDeclarationPartCompliance> findByDeclarationPart(Integer part);

    PGCDeclarationPartCompliance findByDeclarationPartAndDeclarationSpec(Integer part, Integer spec);
}
