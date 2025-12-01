package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Repository
public interface ISWorkflowRepository extends JpaRepository<ISWorkflow, Integer> {
    ISWorkflow findByAttachedTo(Integer id);

    List<ISWorkflow> findByIdIn(Iterable<Integer> varl);

    List<ISWorkflow> findByName(String name);
}
