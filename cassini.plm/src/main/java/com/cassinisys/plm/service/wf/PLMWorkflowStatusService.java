package com.cassinisys.plm.service.wf;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Service
public class PLMWorkflowStatusService implements CrudService<PLMWorkflowStatus, Integer> {

    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;

    @Override
    @Transactional
    public PLMWorkflowStatus create(PLMWorkflowStatus plmWorkflowStatus) {
        checkNotNull(plmWorkflowStatus);
        return workflowStatusRepository.save(plmWorkflowStatus);
    }

    @Override
    @Transactional
    public PLMWorkflowStatus update(PLMWorkflowStatus plmWorkflowStatus) {
        checkNotNull(plmWorkflowStatus);
        checkNotNull(plmWorkflowStatus.getId());
        return workflowStatusRepository.save(plmWorkflowStatus);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        workflowStatusRepository.delete(id);
    }

    @Override
    public PLMWorkflowStatus get(Integer id) {
        checkNotNull(id);
        PLMWorkflowStatus workflowStatus = workflowStatusRepository.findOne(id);
        if (workflowStatus == null) {
            throw new ResourceNotFoundException();
        }
        return workflowStatus;
    }

    @Override
    public List<PLMWorkflowStatus> getAll() {
        return workflowStatusRepository.findAll();
    }

    public List<PLMWorkflowStatus> getByWorkflow(Integer id) {
        return workflowStatusRepository.findByWorkflow(id);
    }

    public List<PLMWorkflowStatus> getMultipleStatuses(List<Integer> ids) {
        return workflowStatusRepository.findByIdIn(ids);
    }

    public List<PLMWorkflowStatus> getStatusesByWorkflow(Integer workflowId) {
        return workflowStatusRepository.findByWorkflow(workflowId);
    }

    public void deleteStatuses (List<PLMWorkflowStatus> statuses) {
        workflowStatusRepository.delete(statuses);
    }
}
