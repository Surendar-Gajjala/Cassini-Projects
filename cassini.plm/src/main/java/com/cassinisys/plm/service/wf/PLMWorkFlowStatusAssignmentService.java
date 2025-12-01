package com.cassinisys.plm.service.wf;

import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.plm.model.wf.PLMWorkFlowStatusAssignment;
import com.cassinisys.plm.repo.wf.PLMWorkFlowStatusAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */

@Service
public class PLMWorkFlowStatusAssignmentService implements CrudService<PLMWorkFlowStatusAssignment, Integer>, PageableService<PLMWorkFlowStatusAssignment, Integer> {

    @Autowired
    PLMWorkFlowStatusAssignmentRepository plmWorkFlowStatusAssignmentRepository;

    @Override
    @Transactional
    public PLMWorkFlowStatusAssignment create(PLMWorkFlowStatusAssignment plmWorkFlowStatusAssignment) {
        return plmWorkFlowStatusAssignmentRepository.save(plmWorkFlowStatusAssignment);
    }

    @Override
    @Transactional
    public PLMWorkFlowStatusAssignment update(PLMWorkFlowStatusAssignment plmBom) {
        return plmWorkFlowStatusAssignmentRepository.save(plmBom);

    }

    @Override
    @Transactional
    public void delete(Integer id) {
        plmWorkFlowStatusAssignmentRepository.delete(id);
    }

    @Override
    public PLMWorkFlowStatusAssignment get(Integer id) {
        return plmWorkFlowStatusAssignmentRepository.findOne(id);
    }

    @Override
    public List<PLMWorkFlowStatusAssignment> getAll() {
        return plmWorkFlowStatusAssignmentRepository.findAll();
    }

    @Override
    public Page<PLMWorkFlowStatusAssignment> findAll(Pageable pageable) {
        return plmWorkFlowStatusAssignmentRepository.findAll(pageable);
    }
}
