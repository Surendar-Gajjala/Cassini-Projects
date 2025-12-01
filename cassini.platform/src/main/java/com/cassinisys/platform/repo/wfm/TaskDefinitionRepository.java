package com.cassinisys.platform.repo.wfm;

import com.cassinisys.platform.model.wfm.TaskDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 2/1/2016.
 */
@Repository
public interface TaskDefinitionRepository extends JpaRepository<TaskDefinition, Integer>{
    List<TaskDefinition> findByActivity(Integer actvId);
}
