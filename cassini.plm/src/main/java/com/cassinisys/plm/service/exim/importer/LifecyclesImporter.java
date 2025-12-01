package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class LifecyclesImporter extends AbstractImporter {
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;

    @Override
    protected void importData(byte[] bytes) {
        try {
            List<PLMLifeCycle> lifecycles = getObjectMapper().readValue(bytes, new TypeReference<List<PLMLifeCycle>>() {
            });
            for (PLMLifeCycle lc : lifecycles) {
                PLMLifeCycle found = lifeCycleRepository.findByName(lc.getName());
                if (found == null) {
                    createNew(lc);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNew(PLMLifeCycle lifecycle) {
        lifecycle.setId(null);
        List<PLMLifeCyclePhase> phases = lifecycle.getPhases();
        lifecycle.setPhases(null);
        lifecycle = lifeCycleRepository.save(lifecycle);
        List<PLMLifeCyclePhase> newPhases = new ArrayList<>();
        for (PLMLifeCyclePhase phase : phases) {
            PLMLifeCyclePhase newPhase = new PLMLifeCyclePhase();
            newPhase.setLifeCycle(lifecycle.getId());
            newPhase.setPhase(phase.getPhase());
            newPhase.setPhaseType(phase.getPhaseType());
            newPhases.add(newPhase);
        }
        newPhases = lifeCyclePhaseRepository.save(newPhases);
        lifecycle.setPhases(newPhases);
        lifeCycleRepository.save(lifecycle);
    }
}
