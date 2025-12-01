package com.cassinisys.drdo.controller.partTracking;

import com.cassinisys.drdo.model.partTracking.MBOMPartTracking;
import com.cassinisys.drdo.service.partTracking.MBOMPartTrackingService;
import com.cassinisys.platform.api.core.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@RestController
@RequestMapping("drdo/mbomPartTracking")
public class MBOMPartTrackingController extends BaseController {

    @Autowired
    private MBOMPartTrackingService mbomPartTrackingService;

    @RequestMapping(method = RequestMethod.GET)
    public List<MBOMPartTracking> getAll() {
        return mbomPartTrackingService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MBOMPartTracking get(@PathVariable("id") Integer id) {
        return mbomPartTrackingService.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        mbomPartTrackingService.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public MBOMPartTracking save(@RequestBody MBOMPartTracking mbomPartTracking) throws Exception {
        return mbomPartTrackingService.create(mbomPartTracking);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public MBOMPartTracking update(@PathVariable("id") Integer id, @RequestBody MBOMPartTracking mbomPartTracking) {
        return mbomPartTrackingService.update(mbomPartTracking);
    }
}
