package com.cassinisys.platform.repo.wfm;

import com.cassinisys.platform.model.wfm.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lakshmi on 2/1/2016.
 */
@Repository
public interface WorkFlowRepository extends JpaRepository<WorkFlow, Integer> {
}