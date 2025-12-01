package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMParamValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyam on 10-06-2020.
 */
@Repository
public interface ParamValueRepository extends JpaRepository<PQMParamValue, Integer> {
}
