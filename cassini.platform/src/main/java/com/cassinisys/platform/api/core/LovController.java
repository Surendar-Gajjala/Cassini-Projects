package com.cassinisys.platform.api.core;

import com.cassinisys.platform.filtering.LovCriteria;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.service.core.LovService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author reddy
 */
@RestController
@RequestMapping("/core/lovs")
@Api(tags = "PLATFORM.CORE",description = "Core endpoints")
public class LovController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private LovService lovService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Lov> getAll() {
        return lovService.getAll();
    }

    @RequestMapping(value = "/map/name", method = RequestMethod.GET)
    public Map<String, Lov> getLovMapWithNameAsKey() {
        return lovService.getLovMapWithNameAsKey();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Lov create(@RequestBody Lov lov) {
        return lovService.create(lov);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Lov update(@PathVariable("id") Integer id, @RequestBody Lov lov) {
        return lovService.update(lov);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Lov get(@PathVariable("id") Integer id) {
        return lovService.get(id);
    }

    @RequestMapping(value = "/getLovById/{id}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getLovById(@PathVariable("id") Integer id) {
        return lovService.getLovById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        lovService.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Lov> getAll(PageRequest pageRequest, LovCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return lovService.find(criteria, pageable);
    }

    @RequestMapping(value = "/personLovs", method = RequestMethod.GET)
    public List<Lov> getAllPersonLovs() {
        return lovService.getAllPersonLovs();
    }

    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    public List<Lov> getAllLovsByType(@PathVariable("type") String type) {
        return lovService.getAllLovsByType(type);
    }

    @RequestMapping(value = "/lovByName/{name}", method = RequestMethod.GET)
    public Lov getLovByName(@PathVariable("name") String name) {
        return lovService.getLovByName(name);
    }

}
