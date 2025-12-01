package com.cassinisys.erp.api.crm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.crm.ERPVehicle;
import com.cassinisys.erp.repo.crm.VehicleRepository;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by reddy on 10/28/15.
 */
@RestController
@RequestMapping("crm/vehicles")
@Api(name = "Vehicle", description = "Vehicle endpoint", group = "CRM")
public class VehicleController extends BaseController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @RequestMapping
    public List<ERPVehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    @RequestMapping (method = RequestMethod.POST)
    public ERPVehicle createVehicle(@RequestBody @Valid ERPVehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @RequestMapping (name = "/{id}", method = RequestMethod.PUT)
    public ERPVehicle updateVehicle(@RequestBody @Valid ERPVehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

}
