package com.cassinisys.is.api.classification;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.service.procm.ClassificationService;
import com.cassinisys.platform.api.core.BaseController;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("classification/{type}")
@Api(name = "Classification", description = "BOM endpoint", group = "IS")
public class ClassificationController extends BaseController {
    @Autowired
    private ClassificationService classificationService;

    /* Types */

    @RequestMapping
    public List<Object> getClassificationTree(@PathVariable("type") ISObjectType type) {
        return classificationService.getClassificationTree(type);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Object create(@PathVariable("type") ISObjectType type,
                         @RequestBody ObjectNode json) {
        return classificationService.createType(type, json);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable("type") ISObjectType type,
                         @RequestBody ObjectNode json) {
        return classificationService.updateType(type, json);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("type") ISObjectType type,
                       @PathVariable("id") Integer id) {
        classificationService.deleteType(type, id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable("type") ISObjectType type,
                      @PathVariable("id") Integer id) {
        return classificationService.getType(type, id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Object> getAll(@PathVariable("type") ISObjectType type) {
        return classificationService.getAllTypes(type);
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    public List<Object> getChildren(@PathVariable("type") ISObjectType type,
                                    @PathVariable("id") Integer id) {
        return classificationService.getChildren(type, id);
    }


    /* Attributes */

    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.POST)
    public Object createAttribute(@PathVariable("type") ISObjectType type,
                                  @RequestBody ObjectNode json) {
        return classificationService.createTypeAttribute(type, json);
    }

    @RequestMapping(value = "/{typeId}/attributes/{id}", method = RequestMethod.PUT)
    public Object updateAttribute(@PathVariable("type") ISObjectType type,
                                  @RequestBody ObjectNode json) {
        return classificationService.updateTypeAttribute(type, json);
    }

    @RequestMapping(value = "/{typeId}/attributes/{id}", method = RequestMethod.DELETE)
    public void deleteAttribute(@PathVariable("type") ISObjectType type,
                                @PathVariable("id") Integer id) {
        classificationService.deleteTypeAttribute(type, id);
    }

    @RequestMapping(value = "/{typeId}/attributes/{id}", method = RequestMethod.GET)
    public Object getAttribute(@PathVariable("type") ISObjectType type,
                               @PathVariable("id") Integer id) {
        return classificationService.getTypeAttribute(type, id);
    }

    @RequestMapping(value = "/{typeId}/attributes")
    public List<Object> getAttributes(@PathVariable("type") ISObjectType type,
                                      @PathVariable("typeId") Integer typeId,
                                      @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return classificationService.getTypeAttributes(type, typeId, hierarchy);
    }
}
