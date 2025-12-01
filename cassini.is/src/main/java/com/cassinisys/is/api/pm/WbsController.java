package com.cassinisys.is.api.pm;
/**
 * The Class is for wbsController
 **/

import com.cassinisys.is.model.pm.ISWbs;
import com.cassinisys.is.service.pm.WbsService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Api(name = "Wbs",
        description = "Wbs endpoint", group = "Project")
@RestController
@RequestMapping("/projects/{projectId}/appWbs")
public class WbsController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private WbsService wbsService;

    /**
     * The method used for creating the ISWbs
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISWbs create(@PathVariable("projectId") Integer projectId,
                        @RequestBody ISWbs wbs) {
        wbs.setId(null);
        wbs.setPercentageComplete(0.0);
        return wbsService.create(wbs);
    }

    /**
     * The method used for updating the ISWbs
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISWbs update(@PathVariable("projectId") Integer projectId,
                        @PathVariable("id") Integer id,
                        @RequestBody ISWbs wbs) {
        wbs.setId(id);
        if (wbs.getPercentageComplete() == null) {
            wbs.setPercentageComplete(0.0);
        }
        return wbsService.update(wbs);
    }

    /**
     * The method used for deleting the ISWbs
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("projectId") Integer projectId,
                       @PathVariable("id") String id) throws IOException {
        BigInteger wbsId = new BigInteger(id);
        //wbsService.deleteWithLinks(projectId, wbsId.intValue());
        wbsService.delete(wbsId.intValue());
    }

    /**
     * The method used get the value of ISWbs
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISWbs get(@PathVariable("projectId") Integer projectId,
                     @PathVariable("id") Integer id) {
        return wbsService.get(id);
    }

    /**
     * The method used getall the values of ISWbs
     **/
    @RequestMapping(method = RequestMethod.GET)
    public Page<ISWbs> getAll(@PathVariable("projectId") Integer projectId,
                              PageRequest page) {
        Pageable pageable = pageRequestConverter.convert(page);
        return wbsService.findAll(pageable);
    }

    /**
     * The method used to getClassificationTree  of ISWbs using projectId
     **/
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<ISWbs> getClassificationTree(@PathVariable("projectId") Integer projectId) {
        return wbsService.getWbsTree(projectId);
    }

    /**
     * The method used getMultiples  of ISWbs
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISWbs> getMultiple(@PathVariable("projectId") Integer projectId,
                                   @PathVariable Integer[] ids) {
        return wbsService.findMultiple(Arrays.asList(ids));
    }

    /**
     * The method used find the ISWbs by  project
     **/
    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISWbs> findByProject(@PathVariable("projectId") Integer projectId, PageRequest pageRequest) {
        return wbsService.findByProject(projectId, pageRequest);
    }

}
