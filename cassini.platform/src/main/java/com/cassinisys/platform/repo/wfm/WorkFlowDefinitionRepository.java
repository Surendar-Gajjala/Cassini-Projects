package com.cassinisys.platform.repo.wfm;

import com.cassinisys.platform.model.wfm.WorkFlowDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lakshmi on 2/1/2016.
 */

@Repository
public interface WorkFlowDefinitionRepository extends JpaRepository<WorkFlowDefinition, Integer> {
}