package com.cassinisys.drdo.controller.partTracking;

import com.cassinisys.drdo.model.partTracking.PartTracking;
import com.cassinisys.drdo.model.partTracking.PartTrackingSteps;
import com.cassinisys.drdo.service.partTracking.PartTrackingService;
import com.cassinisys.platform.api.core.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@RestController
@RequestMapping("drdo/lists")
public class PartTrackingController extends BaseController {

    @Autowired
    private PartTrackingService partTrackingService;

    @RequestMapping(method = RequestMethod.GET)
    public List<PartTracking> getAll() {
        return partTrackingService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PartTracking get(@PathVariable("id") Integer id) {
        return partTrackingService.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        partTrackingService.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public PartTracking save(@RequestBody PartTracking tracking) {
        return partTrackingService.create(tracking);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public PartTracking update(@PathVariable("id") Integer id, @RequestBody PartTracking tracking) {
        return partTrackingService.update(tracking);
    }

    @RequestMapping(value = "/steps/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PartTrackingSteps> getMultiplePartTrackings(@PathVariable("ids") Integer[] ids) {
        return partTrackingService.getStepsMultiple(Arrays.asList(ids));
    }

}
