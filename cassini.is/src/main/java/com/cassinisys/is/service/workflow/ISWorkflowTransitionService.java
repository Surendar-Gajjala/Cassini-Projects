package com.cassinisys.is.service.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowTransition;
import com.cassinisys.is.repo.workflow.ISWorkflowTransitionRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Service
public class ISWorkflowTransitionService implements CrudService<ISWorkflowTransition, Integer> {

    @Autowired
    private ISWorkflowTransitionRepository workflowTransitionRepository;

    @Override
    @Transactional
    public ISWorkflowTransition create(ISWorkflowTransition workflowTransition) {
        checkNotNull(workflowTransition);
        return workflowTransitionRepository.save(workflowTransition);
    }

    @Override
    @Transactional
    public ISWorkflowTransition update(ISWorkflowTransition workflowTransition) {
        checkNotNull(workflowTransition);
        checkNotNull(workflowTransition.getId());
        return workflowTransitionRepository.save(workflowTransition);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        workflowTransitionRepository.delete(id);
    }

    @Override
    public ISWorkflowTransition get(Integer id) {
        checkNotNull(id);
        ISWorkflowTransition workflowTransition = workflowTransitionRepository.findOne(id);
        if (workflowTransition == null) {
            throw new ResourceNotFoundException();
        }
        return workflowTransition;
    }

    @Override
    public List<ISWorkflowTransition> getAll() {
        return workflowTransitionRepository.findAll();
    }

    public List<ISWorkflowTransition> getByFromStatus(Integer id) {
        return workflowTransitionRepository.findByFromStatus(id);
    }

    public List<ISWorkflowTransition> getByToStatus(Integer id) {
        return workflowTransitionRepository.findByToStatus(id);
    }

    public List<ISWorkflowTransition> getByWorkflow(Integer id) {
        return workflowTransitionRepository.findByWorkflow(id);
    }

    public List<ISWorkflowTransition> getMultipleTransitions(List<Integer> ids) {
        return workflowTransitionRepository.findByIdIn(ids);
    }
}
