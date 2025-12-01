package com.cassinisys.drdo.service.partTracking;

import com.cassinisys.drdo.model.partTracking.MBOMPartTracking;
import com.cassinisys.drdo.repo.partTracking.MBOMPartTrackingRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Service
public class MBOMPartTrackingService implements CrudService<MBOMPartTracking, Integer>,
        PageableService<MBOMPartTracking, Integer> {

    @Autowired
    private MBOMPartTrackingRepository mbomPartTrackingRepository;

    @Override
    @Transactional(readOnly = false)
    public MBOMPartTracking create(MBOMPartTracking mbomPartTracking) {
        checkNotNull(mbomPartTracking);
        mbomPartTrackingRepository.save(mbomPartTracking);
        return mbomPartTracking;
    }

    @Override
    @Transactional(readOnly = false)
    public MBOMPartTracking update(MBOMPartTracking mbomPartTracking) {
        checkNotNull(mbomPartTracking);
        mbomPartTracking = mbomPartTrackingRepository.save(mbomPartTracking);
        return mbomPartTracking;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        MBOMPartTracking mbomPartTracking = mbomPartTrackingRepository.findOne(id);
        if (mbomPartTracking == null) {
            throw new ResourceNotFoundException();
        }
        mbomPartTrackingRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MBOMPartTracking get(Integer id) {
        checkNotNull(id);
        MBOMPartTracking mbomPartTracking = mbomPartTrackingRepository.findOne(id);
        return mbomPartTracking;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MBOMPartTracking> getAll() {
        return mbomPartTrackingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MBOMPartTracking> findAll(Pageable pageable) {
        return mbomPartTrackingRepository.findAll(pageable);
    }
}

