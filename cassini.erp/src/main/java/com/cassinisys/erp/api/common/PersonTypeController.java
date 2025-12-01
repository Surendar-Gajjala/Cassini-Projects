package com.cassinisys.erp.api.common;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.common.ERPPersonType;
import com.cassinisys.erp.repo.common.PersonTypeRepository;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by reddy on 10/2/15.
 */

@RestController
@RequestMapping("common/persontypes")
@Api(name="PersonTypes",description="PersonTypes endpoint",group="COMMON")
public class PersonTypeController extends BaseController {
    @Autowired
    private PersonTypeRepository personTypeRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<ERPPersonType> getAll() {
        return personTypeRepository.findAll();
    }
}
