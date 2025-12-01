package com.cassinisys.is.service.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowDefinitionStatus;
import com.cassinisys.is.repo.workflow.ISWorkflowDefinitionStatusRepository;
import com.cassinisys.is.repo.workflow.ISWorkflowDefinitionTerminateRepository;
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
public class ISWorkflowDefinitionStatusService implements CrudService<ISWorkflowDefinitionStatus, Integer> {

    @Autowired
    private ISWorkflowDefinitionStatusRepository isWorkflowDefinitionStatusRepository;
    @Autowired
    private ISWorkflowDefinitionTerminateRepository isWorkflowDefinitionTerminateRepository;

    @Override
    @Transactional
    public ISWorkflowDefinitionStatus create(ISWorkflowDefinitionStatus isWorkflowDefinitionStatus) {
        checkNotNull(isWorkflowDefinitionStatus);
        return isWorkflowDefinitionStatusRepository.save(isWorkflowDefinitionStatus);
    }

    @Override
    @Transactional
    public ISWorkflowDefinitionStatus update(ISWorkflowDefinitionStatus isWorkflowDefinitionStatus) {
        checkNotNull(isWorkflowDefinitionStatus);
        return isWorkflowDefinitionStatusRepository.save(isWorkflowDefinitionStatus);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        isWorkflowDefinitionStatusRepository.delete(id);

    }

    @Override
    public ISWorkflowDefinitionStatus get(Integer id) {
        checkNotNull(id);
        ISWorkflowDefinitionStatus plmWorkflowDefinitionStatus = isWorkflowDefinitionStatusRepository.findOne(id);
        if (plmWorkflowDefinitionStatus == null) {
            throw new ResourceNotFoundException();
        }
        return plmWorkflowDefinitionStatus;
    }

    @Override
    public List<ISWorkflowDefinitionStatus> getAll() {
        return isWorkflowDefinitionStatusRepository.findAll();
    }

    public List<ISWorkflowDefinitionStatus> getByWorkflow(Integer id) {
        return isWorkflowDefinitionStatusRepository.findByWorkflow(id);
    }
}
