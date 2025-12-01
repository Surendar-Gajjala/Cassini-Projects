package com.cassinisys.is.service.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowStatus;
import com.cassinisys.is.repo.workflow.ISWorkflowStatusRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by GSR on 015 20-May -17.
 */
@Service
public class ISWorkflowStatusService implements CrudService<ISWorkflowStatus, Integer> {

    @Autowired
    private ISWorkflowStatusRepository workflowStatusRepository;

    @Override
    @Transactional
    public ISWorkflowStatus create(ISWorkflowStatus isWorkflowStatus) {
        checkNotNull(isWorkflowStatus);
        return workflowStatusRepository.save(isWorkflowStatus);
    }

    @Override
    @Transactional
    public ISWorkflowStatus update(ISWorkflowStatus isWorkflowStatus) {
        checkNotNull(isWorkflowStatus);
        checkNotNull(isWorkflowStatus.getId());
        return workflowStatusRepository.save(isWorkflowStatus);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        workflowStatusRepository.delete(id);
    }

    @Override
    public ISWorkflowStatus get(Integer id) {
        checkNotNull(id);
        ISWorkflowStatus workflowStatus = workflowStatusRepository.findOne(id);
        if (workflowStatus == null) {
            throw new ResourceNotFoundException();
        }
        return workflowStatus;
    }

    @Override
    public List<ISWorkflowStatus> getAll() {
        return workflowStatusRepository.findAll();
    }

    public List<ISWorkflowStatus> getByWorkflow(Integer id) {
        return workflowStatusRepository.findByWorkflow(id);
    }

    public List<ISWorkflowStatus> getMultipleStatuses(List<Integer> ids) {
        return workflowStatusRepository.findByIdIn(ids);
    }

    public List<ISWorkflowStatus> getStatusesByWorkflow(Integer workflowId) {
        return workflowStatusRepository.findByWorkflow(workflowId);
    }
}
