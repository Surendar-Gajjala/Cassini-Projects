package com.cassinisys.platform.api.core;

import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.service.core.MeasurementService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyam on 18-05-2020.
 */
@RestController
@RequestMapping("/measurements")
@Api(tags = "PLATFORM.CORE", description = "Core endpoints")
public class MeasurementController extends BaseController {

    @Autowired
    private MeasurementService measurementService;

    @RequestMapping(method = RequestMethod.POST)
    public Measurement create(@RequestBody Measurement measurement) {
        return measurementService.create(measurement);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Measurement update(@PathVariable("id") Integer id, @RequestBody Measurement measurement) {
        return measurementService.update(measurement);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        measurementService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Measurement get(@PathVariable("id") Integer id) {
        return measurementService.get(id);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public Measurement getMeasurementByName(@PathVariable("name") String name) {
        return measurementService.getMeasurementByName(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Measurement> getAll() {
        return measurementService.getAll();
    }

    @RequestMapping(value = "/{id}/units", method = RequestMethod.POST)
    public MeasurementUnit createMeasurementUnit(@PathVariable("id") Integer id, @RequestBody MeasurementUnit measurementUnit) {
        return measurementService.createMeasurementUnit(id, measurementUnit);
    }

    @RequestMapping(value = "/{id}/units/{unitId}}", method = RequestMethod.PUT)
    public MeasurementUnit updateMeasurementUnit(@PathVariable("id") Integer id, @PathVariable("unitId") Integer unitId,
                                                 @RequestBody MeasurementUnit measurementUnit) {
        return measurementService.updateMeasurementUnit(id, unitId, measurementUnit);
    }

    @RequestMapping(value = "/{id}/units/{unitId}}", method = RequestMethod.DELETE)
    public void deleteMeasurementUnit(@PathVariable("id") Integer id, @PathVariable("unitId") Integer unitId) {
        measurementService.deleteMeasurementUnit(id, unitId);
    }
}
