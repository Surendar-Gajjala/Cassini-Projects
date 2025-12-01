package com.cassinisys.erp.api.common;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.common.ERPMobileDevice;
import com.cassinisys.erp.model.security.ERPSession;
import com.cassinisys.erp.repo.security.SessionRepository;
import com.cassinisys.erp.service.common.MobileDeviceService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by reddy on 10/24/15.
 */
@RestController
@RequestMapping("common/mobiledevice")
@Api(name="Mobile Device",description="Mobile device endpoint",group="COMMON")
public class MobileDeviceController extends BaseController {

    @Autowired
    private MobileDeviceService mobileDeviceService;

    @Autowired
    private SessionRepository sessionRepository;


    @RequestMapping (method = RequestMethod.GET)
    public List<ERPMobileDevice> getAll() {
        return mobileDeviceService.getAll();
    }

    @RequestMapping (method = RequestMethod.POST)
    public ERPMobileDevice create(@RequestBody ERPMobileDevice mobileDevice) {
        ERPMobileDevice device = mobileDeviceService.create(mobileDevice);

        ERPSession session = getSessionWrapper().getSession();
        if (session != null && session.getLogin() != null) {
            session.setMobileDevice(device);
            sessionRepository.save(session);
        }

        return device;
    }

    @RequestMapping (value = "/{id}", method = RequestMethod.PUT)
    public ERPMobileDevice update(@RequestBody ERPMobileDevice mobileDevice) {
        ERPMobileDevice device = mobileDeviceService.create(mobileDevice);

        ERPSession session = getSessionWrapper().getSession();
        if (session != null && session.getLogin() != null) {
            session.setMobileDevice(device);
            sessionRepository.save(session);
        }

        return device;
    }
}
