package com.cassinisys.plm.service.wf;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinitionTransition;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionTransitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by GSR on 19-05-2017.
 */
@Service
public class PLMWorkflowDefinitionTransitionService implements CrudService<PLMWorkflowDefinitionTransition, Integer> {

    @Autowired
    private PLMWorkflowDefinitionTransitionRepository plmWorkflowDefinitionTransitionRepository;

    @Override
    @Transactional
    public PLMWorkflowDefinitionTransition create(PLMWorkflowDefinitionTransition plmWorkflowDefinitionTransition) {
        checkNotNull(plmWorkflowDefinitionTransition);
        return plmWorkflowDefinitionTransitionRepository.save(plmWorkflowDefinitionTransition);
    }

    @Override
    @Transactional
    public PLMWorkflowDefinitionTransition update(PLMWorkflowDefinitionTransition plmWorkflowDefinitionTransition) {
        checkNotNull(plmWorkflowDefinitionTransition);
        return plmWorkflowDefinitionTransitionRepository.save(plmWorkflowDefinitionTransition);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (id != null) {
            plmWorkflowDefinitionTransitionRepository.delete(id);
        }
    }

    @Override
    public PLMWorkflowDefinitionTransition get(Integer id) {
        PLMWorkflowDefinitionTransition plmWorkflowDefinitionTransition = plmWorkflowDefinitionTransitionRepository.findOne(id);
        if (plmWorkflowDefinitionTransition == null) {
            throw new ResourceNotFoundException();
        }
        return plmWorkflowDefinitionTransition;
    }

    @Override
    public List<PLMWorkflowDefinitionTransition> getAll() {
        return plmWorkflowDefinitionTransitionRepository.findAll();
    }

    public List<PLMWorkflowDefinitionTransition> getByFromStatus(Integer id) {
        return plmWorkflowDefinitionTransitionRepository.findByFromStatus(id);
    }

    public List<PLMWorkflowDefinitionTransition> getByToStatus(Integer id) {
        return plmWorkflowDefinitionTransitionRepository.findByToStatus(id);
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinitionTransition> getByWorkflow(Integer id) {
        return plmWorkflowDefinitionTransitionRepository.findByWorkflow(id);
    }

    @Transactional
    public void deleteWfDefTransition(Integer wfDefId, Integer transition) {
        plmWorkflowDefinitionTransitionRepository.delete(transition);
    }

}
