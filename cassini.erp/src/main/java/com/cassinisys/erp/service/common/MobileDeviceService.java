package com.cassinisys.erp.service.common;

import com.cassinisys.erp.model.common.ERPMobileDevice;
import com.cassinisys.erp.repo.common.MobileDeviceRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by reddy on 10/24/15.
 */
@Service
public class MobileDeviceService implements CrudService<ERPMobileDevice, String>,
        PageableService<ERPMobileDevice, String> {

    @Autowired
    private MobileDeviceRepository mobileDeviceRepository;


    @Override
    public ERPMobileDevice create(ERPMobileDevice mobileDevice) {
        return mobileDeviceRepository.save(mobileDevice);
    }

    @Override
    public ERPMobileDevice update(ERPMobileDevice mobileDevice) {
        return mobileDeviceRepository.save(mobileDevice);
    }

    @Override
    public void delete(String id) {
        mobileDeviceRepository.delete(id);
    }

    @Override
    public ERPMobileDevice get(String id) {
        return mobileDeviceRepository.findOne(id);
    }

    @Override
    public List<ERPMobileDevice> getAll() {
        return mobileDeviceRepository.findAll();
    }

    @Override
    public Page<ERPMobileDevice> findAll(Pageable pageable) {
        return mobileDeviceRepository.findAll(pageable);
    }
}
