package com.cassinisys.drdo.service.partTracking;

import com.cassinisys.drdo.model.partTracking.PartTrackingScannedUPN;
import com.cassinisys.drdo.repo.partTracking.PartTrackingScannedUPNRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Service
public class PartTrackingScannedUPNService implements CrudService<PartTrackingScannedUPN, Integer>,
        PageableService<PartTrackingScannedUPN, Integer> {

    @Autowired
    private PartTrackingScannedUPNRepository scannedUpnRepository;

    @Override
    public PartTrackingScannedUPN create(PartTrackingScannedUPN scannedUPN) {
        return scannedUpnRepository.save(scannedUPN);
    }

    @Override
    public PartTrackingScannedUPN update(PartTrackingScannedUPN PartTrackingScannedUPN) {
        return scannedUpnRepository.save(PartTrackingScannedUPN);
    }

    @Override
    public void delete(Integer integer) {
        scannedUpnRepository.delete(integer);
    }

    @Override
    public PartTrackingScannedUPN get(Integer integer) {
        return scannedUpnRepository.findOne(integer);
    }

    @Override
    public List<PartTrackingScannedUPN> getAll() {
        return scannedUpnRepository.findAll();
    }

    @Override
    public Page<PartTrackingScannedUPN> findAll(Pageable pageable) {
        return scannedUpnRepository.findAll(pageable);
    }

    public List<PartTrackingScannedUPN> findByTrackStep(Integer integer) {
        List<PartTrackingScannedUPN> PartTrackingScannedUpns = scannedUpnRepository.findByPartTrackingId(integer);
        return PartTrackingScannedUpns;
    }

    public void failScannedPartByUpn(String upn) {
        List<PartTrackingScannedUPN> PartTrackingScannedUpns = scannedUpnRepository.findByUpn(upn);
        for (PartTrackingScannedUPN partTrackingScannedUpn : PartTrackingScannedUpns) {
            partTrackingScannedUpn.setFail(Boolean.TRUE);
            scannedUpnRepository.save(partTrackingScannedUpn);
        }
    }

    public List<PartTrackingScannedUPN> saveScannedPartTrackingUpns(List<PartTrackingScannedUPN> PartTrackingScannedUpns) {
        List<PartTrackingScannedUPN> partTrackingScannedUpns = scannedUpnRepository.save(PartTrackingScannedUpns);
        return partTrackingScannedUpns;
    }
}