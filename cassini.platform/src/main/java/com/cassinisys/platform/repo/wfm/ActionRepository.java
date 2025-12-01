package com.cassinisys.platform.repo.wfm;

import com.cassinisys.platform.model.wfm.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 2/1/2016.
 */
@Repository
public interface ActionRepository extends JpaRepository<Action, Integer> {
	List<Action> findBySource(Integer actvId);
	List<Action> findByTarget(Integer actvId);
}