package com.cassinisys.platform.api.core;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.core.ObjectService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@RestController
@RequestMapping("/core/objects")
@Api(tags = "PLATFORM.CORE",description = "Core endpoints")
public class ObjectController extends BaseController {

    @Autowired
    private ObjectService objectService;

    @RequestMapping(value = "/{type}",method = RequestMethod.GET)
    public List<CassiniObject> getByObjectType(@PathVariable ObjectType type) {
        return objectService.getByObjectType(type);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<CassiniObject> getAllObjects() {
        return objectService.getAll();
    }

    @RequestMapping(value = "/id/{id}",method = RequestMethod.GET)
    public CassiniObject getByObject(@PathVariable Integer id) {
        return objectService.get(id);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<CassiniObject> getMultipleObjects(@PathVariable Integer[] ids,
                                           @RequestParam String type) {
        return objectService.findMultipleByType(Arrays.asList(ids), type);
    }

    @RequestMapping
    public CassiniObject getObjectByTypeAndId(@RequestParam String type, @RequestParam Integer id) {
        return objectService.findByObjectTypeAndId(type, id);
    }
}
