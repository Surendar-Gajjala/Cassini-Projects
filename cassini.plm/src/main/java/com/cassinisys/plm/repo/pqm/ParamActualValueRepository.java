package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMParamActualValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 18-06-2020.
 */
@Repository
public interface ParamActualValueRepository extends JpaRepository<PQMParamActualValue, Integer> {
    List<PQMParamActualValue> findByChecklistOrderByIdAsc(Integer id);
}
