package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCPartSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCPartSpecificationRepository extends JpaRepository<PGCPartSpecification, Integer> {

}
