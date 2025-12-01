package com.cassinisys.plm.service.wf;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinitionStatus;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionStatusRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionTerminateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by GSR on 19-05-2017.
 */
@Service
public class PLMWorkflowDefinitionStatusService implements CrudService<PLMWorkflowDefinitionStatus, Integer> {

    @Autowired
    private PLMWorkflowDefinitionStatusRepository plmWorkflowDefinitionStatusRepository;
    @Autowired
    private PLMWorkflowDefinitionTerminateRepository plmWorkflowDefinitionTerminateRepository;

    @Override
    @Transactional
    public PLMWorkflowDefinitionStatus create(PLMWorkflowDefinitionStatus plmWorkflowDefinitionStatus) {
        checkNotNull(plmWorkflowDefinitionStatus);
        return plmWorkflowDefinitionStatusRepository.save(plmWorkflowDefinitionStatus);
    }

    @Override
    @Transactional
    public PLMWorkflowDefinitionStatus update(PLMWorkflowDefinitionStatus plmWorkflowDefinitionStatus) {
        checkNotNull(plmWorkflowDefinitionStatus);
        return plmWorkflowDefinitionStatusRepository.save(plmWorkflowDefinitionStatus);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (id != null) {
            plmWorkflowDefinitionStatusRepository.delete(id);
        }

    }

    @Override
    public PLMWorkflowDefinitionStatus get(Integer id) {
        checkNotNull(id);
        PLMWorkflowDefinitionStatus plmWorkflowDefinitionStatus = plmWorkflowDefinitionStatusRepository.findOne(id);
        if (plmWorkflowDefinitionStatus == null) {
            throw new ResourceNotFoundException();
        }
        return plmWorkflowDefinitionStatus;
    }

    @Override
    public List<PLMWorkflowDefinitionStatus> getAll() {
        return plmWorkflowDefinitionStatusRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinitionStatus> getByWorkflow(Integer id) {
        return plmWorkflowDefinitionStatusRepository.findByWorkflow(id);
    }

    @Transactional
    public void deleteWfDefStatus(Integer wfDefId, Integer definitionStatusId) {
        plmWorkflowDefinitionStatusRepository.delete(definitionStatusId);
    }
}
