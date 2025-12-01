package com.cassinisys.platform.repo.wfm;

import com.cassinisys.platform.model.wfm.WorkflowTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 2/1/2016.
 */
    @Repository
    public interface WorkflowTaskRepository extends JpaRepository<WorkflowTask, Integer>{
    	List<WorkflowTask> findByActivity(Integer actvId);
}
