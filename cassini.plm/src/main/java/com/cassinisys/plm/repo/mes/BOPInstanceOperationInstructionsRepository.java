package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPInstanceOperationInstructions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by smukka on 28-07-2022.
 */
@Repository
public interface BOPInstanceOperationInstructionsRepository extends JpaRepository<MESBOPInstanceOperationInstructions, Integer> {
    MESBOPInstanceOperationInstructions findByBopOperation(Integer bopOperation);
}
