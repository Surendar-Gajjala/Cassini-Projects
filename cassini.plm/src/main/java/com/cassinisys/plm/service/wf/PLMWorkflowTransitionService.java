package com.cassinisys.plm.service.wf;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.plm.model.wf.PLMWorkflowTransition;
import com.cassinisys.plm.repo.wf.PLMWorkflowTransitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Service
public class PLMWorkflowTransitionService implements CrudService<PLMWorkflowTransition, Integer> {

    @Autowired
    private PLMWorkflowTransitionRepository workflowTransitionRepository;

    @Override
    @Transactional
    public PLMWorkflowTransition create(PLMWorkflowTransition workflowTransition) {
        checkNotNull(workflowTransition);
        return workflowTransitionRepository.save(workflowTransition);
    }

    @Override
    @Transactional
    public PLMWorkflowTransition update(PLMWorkflowTransition workflowTransition) {
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
    public PLMWorkflowTransition get(Integer id) {
        checkNotNull(id);
        PLMWorkflowTransition workflowTransition = workflowTransitionRepository.findOne(id);
        if (workflowTransition == null) {
            throw new ResourceNotFoundException();
        }
        return workflowTransition;
    }

    @Override
    public List<PLMWorkflowTransition> getAll() {
        return workflowTransitionRepository.findAll();
    }

    public List<PLMWorkflowTransition> getByFromStatus(Integer id) {
        return workflowTransitionRepository.findByFromStatus(id);
    }

    public List<PLMWorkflowTransition> getByToStatus(Integer id) {
        return workflowTransitionRepository.findByToStatus(id);
    }

    public List<PLMWorkflowTransition> getByWorkflow(Integer id) {
        return workflowTransitionRepository.findByWorkflow(id);
    }

    public List<PLMWorkflowTransition> getMultipleTransitions(List<Integer> ids) {
        return workflowTransitionRepository.findByIdIn(ids);
    }

    public void deleteTransitions(List<PLMWorkflowTransition> transitions) {
        workflowTransitionRepository.delete(transitions);
    }
}
