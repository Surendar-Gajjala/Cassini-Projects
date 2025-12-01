package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.PersonType;
import com.cassinisys.platform.service.common.PersonTypeService;
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
 * @author reddy
 */
@RestController
@RequestMapping("/common/persontypes")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class PersonTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private PersonTypeService personTypeService;

    @RequestMapping(method = RequestMethod.POST)
    public PersonType create(@RequestBody PersonType personType) {
        personType.setId(null);
        return personTypeService.create(personType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PersonType update(@PathVariable("id") Integer id,
                             @RequestBody PersonType personType) {
        personType.setId(id);
        return personTypeService.update(personType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        personTypeService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PersonType get(@PathVariable("id") Integer id) {
        return personTypeService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PersonType> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return personTypeService.findAll(pageable);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<PersonType> getAll() {
        return personTypeService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PersonType> getMultiple(@PathVariable Integer[] ids) {
        return personTypeService.findMultiple(Arrays.asList(ids));
    }

}
