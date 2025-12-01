package com.cassinisys.drdo.service.partTracking;

import com.cassinisys.drdo.model.partTracking.PartTracking;
import com.cassinisys.drdo.model.partTracking.PartTrackingSteps;
import com.cassinisys.drdo.repo.partTracking.PartTrackingRepository;
import com.cassinisys.drdo.repo.partTracking.PartTrackingStepsRepository;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Service
public class PartTrackingService implements CrudService<PartTracking, Integer> {

    @Autowired
    private PartTrackingRepository partTrackingRepository;

    @Autowired
    private PartTrackingStepsRepository partTrackingStepsRepository;

    @Transactional(readOnly = true)
    public PartTracking get(Integer id) {
        PartTracking partTracking = partTrackingRepository.getOne(id);
        partTracking.setTrackingSteps(partTrackingStepsRepository.findByPartTrackingOrderBySerialNo(id));
        return partTracking;
    }

    @Transactional(readOnly = true)
    public List<PartTracking> getAll() {
        List<PartTracking> partTrackings = partTrackingRepository.findAll();
        for (PartTracking partTracking : partTrackings) {
            partTracking.setTrackingSteps(partTrackingStepsRepository.findByPartTrackingOrderBySerialNo(partTracking.getId()));
        }
        return partTrackings;
    }

    @Transactional(readOnly = false)
    public PartTracking create(PartTracking partTracking) {
        List<PartTrackingSteps> partTrackingStepses = partTracking.getTrackingSteps();
        partTrackingStepses = partTrackingStepsRepository.save(partTrackingStepses);
        PartTracking partTracking1 = partTrackingRepository.save(partTracking);
        partTracking1.setTrackingSteps(partTrackingStepses);
        return partTracking1;
    }

    @Transactional(readOnly = false)
    public PartTracking update(PartTracking partTracking) {
        List<PartTrackingSteps> partTrackingStepses1 = partTrackingStepsRepository.findByPartTrackingOrderBySerialNo(partTracking.getId());
        List<PartTrackingSteps> partTrackingStepses = partTracking.getTrackingSteps();
        if (partTrackingStepses != null) {
            if (partTrackingStepses1.size() > partTrackingStepses.size()) {
                for (PartTrackingSteps trackStep : partTrackingStepses1) {
                    if (!partTrackingStepses.contains(trackStep)) {
                        partTrackingStepsRepository.delete(trackStep.getId());
                    }
                }
            }
        }
        partTrackingStepses = partTrackingStepsRepository.save(partTrackingStepses);
        PartTracking partTracking1 = partTrackingRepository.save(partTracking);
        partTracking1.setTrackingSteps(partTrackingStepses);
        return partTracking1;
    }

    @Transactional(readOnly = false)
    public void delete(Integer id) {
        if (id != null) {
            partTrackingStepsRepository.deleteByPartTracking(id);
            partTrackingRepository.delete(id);
        }
    }

    @Transactional(readOnly = true)
    public List<PartTrackingSteps> getStepsMultiple(List<Integer> integers) {
        return partTrackingStepsRepository.findByIdIn(integers);
    }
}