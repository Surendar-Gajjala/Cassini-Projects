package com.cassinisys.platform.repo.wfm;

import com.cassinisys.platform.model.wfm.Action;
import com.cassinisys.platform.model.wfm.ActionDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 2/1/2016.
 */

@Repository
public interface ActionDefinitionRepository extends JpaRepository<ActionDefinition, Integer> {
	List<ActionDefinition> findBySource(Integer actvId);
	List<Action> findByTarget(Integer actvId);
}