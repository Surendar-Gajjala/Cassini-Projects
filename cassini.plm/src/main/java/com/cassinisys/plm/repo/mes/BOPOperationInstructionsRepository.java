package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPOperationInstructions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by smukka on 28-07-2022.
 */
@Repository
public interface BOPOperationInstructionsRepository extends JpaRepository<MESBOPOperationInstructions, Integer> {
    MESBOPOperationInstructions findByBopOperation(Integer bopOperation);
}
