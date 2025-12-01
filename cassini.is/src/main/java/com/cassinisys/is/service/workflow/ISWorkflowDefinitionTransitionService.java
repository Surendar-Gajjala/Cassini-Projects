package com.cassinisys.is.service.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowDefinitionTransition;
import com.cassinisys.is.repo.workflow.ISWorkflowDefinitionTransitionRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by GSR on 19-05-2017.
 */
@Service
public class ISWorkflowDefinitionTransitionService implements CrudService<ISWorkflowDefinitionTransition, Integer> {

    @Autowired
    private ISWorkflowDefinitionTransitionRepository isWorkflowDefinitionTransitionRepository;

    @Override
    @Transactional
    public ISWorkflowDefinitionTransition create(ISWorkflowDefinitionTransition isWorkflowDefinitionTransition) {
        checkNotNull(isWorkflowDefinitionTransition);
        return isWorkflowDefinitionTransitionRepository.save(isWorkflowDefinitionTransition);
    }

    @Override
    @Transactional
    public ISWorkflowDefinitionTransition update(ISWorkflowDefinitionTransition isWorkflowDefinitionTransition) {
        checkNotNull(isWorkflowDefinitionTransition);
        return isWorkflowDefinitionTransitionRepository.save(isWorkflowDefinitionTransition);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        isWorkflowDefinitionTransitionRepository.delete(id);
    }

    @Override
    public ISWorkflowDefinitionTransition get(Integer id) {
        ISWorkflowDefinitionTransition isWorkflowDefinitionTransition = isWorkflowDefinitionTransitionRepository.findOne(id);
        if (isWorkflowDefinitionTransition == null) {
            throw new ResourceNotFoundException();
        }
        return isWorkflowDefinitionTransition;
    }

    @Override
    public List<ISWorkflowDefinitionTransition> getAll() {
        return isWorkflowDefinitionTransitionRepository.findAll();
    }

    public List<ISWorkflowDefinitionTransition> getByFromStatus(Integer id) {
        return isWorkflowDefinitionTransitionRepository.findByFromStatus(id);
    }

    public List<ISWorkflowDefinitionTransition> getByToStatus(Integer id) {
        return isWorkflowDefinitionTransitionRepository.findByToStatus(id);
    }

    public List<ISWorkflowDefinitionTransition> getByWorkflow(Integer id) {
        return isWorkflowDefinitionTransitionRepository.findByWorkflow(id);
    }

}
