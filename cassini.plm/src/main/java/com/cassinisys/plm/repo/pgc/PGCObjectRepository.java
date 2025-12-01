package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by GSR on 26-10-2020.
 */
@Repository
public interface PGCObjectRepository extends JpaRepository<PGCObject, Integer> {
}
