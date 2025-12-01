package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.MobileDevice;
import com.cassinisys.platform.service.common.MobileDeviceService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Nageshreddy on 31-10-2020.
 */
@RestController
@RequestMapping("/common/mobile/devices")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class MobileDeviceController extends BaseController {

    @Autowired
    private PageRequestConverter pageConverter;

    @Autowired
    private MobileDeviceService mobileDeviceService;

    @RequestMapping(method = RequestMethod.POST)
    public MobileDevice create(@RequestBody MobileDevice mobileDevice) {
        mobileDevice.setDeviceId(null);
        return mobileDeviceService.create(mobileDevice);
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.PUT)
    public MobileDevice update(@PathVariable("deviceId") String deviceId,
                        @RequestBody MobileDevice mobileDevice) {
        mobileDevice.setDeviceId(deviceId);
        return mobileDeviceService.update(mobileDevice);
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("deviceId") String deviceId) {
        mobileDeviceService.delete(deviceId);
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
    public MobileDevice get(@PathVariable("deviceId") String deviceId) {
        return mobileDeviceService.get(deviceId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<MobileDevice> getAll(PageRequest page) {
        Pageable pageable = pageConverter.convert(page);
        return mobileDeviceService.findAll(pageable);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MobileDevice> getMultiple(@PathVariable String[] ids ) {
        return mobileDeviceService.findMultiple(Arrays.asList(ids));
    }

}

