package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Repository
public interface ISWorkflowStatusRepository extends JpaRepository<ISWorkflowStatus, Integer> {
    List<ISWorkflowStatus> findByWorkflow(Integer id);

    List<ISWorkflowStatus> findByIdIn(Iterable<Integer> varl);
}
