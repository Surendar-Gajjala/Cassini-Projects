package com.cassinisys.plm.controller.pgc;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.SubstanceCriteria;
import com.cassinisys.plm.model.pgc.PGCSubstance;
import com.cassinisys.plm.service.pgc.SubstanceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by GSR on 27-10-2020.
 */
@RestController
@RequestMapping("/pgc/substances")
@Api(tags = "PLM.PGC", description = "PGC Related")
public class SubstanceController extends BaseController {


    @Autowired
    private SubstanceService substanceService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PGCSubstance create(@RequestBody PGCSubstance substance) {
        return substanceService.create(substance);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PGCSubstance update(@PathVariable("id") Integer id,
                               @RequestBody PGCSubstance substance) {
        return substanceService.update(substance);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        substanceService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PGCSubstance get(@PathVariable("id") Integer id) {
        return substanceService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PGCSubstance> getAll() {
        return substanceService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PGCSubstance> getMultiple(@PathVariable Integer[] ids) {
        return substanceService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<PGCSubstance> getAllSubstances(PageRequest pageRequest, SubstanceCriteria substanceCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return substanceService.getAllSubstances(pageable, substanceCriteria);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<PGCSubstance> getObjectsByType(@PathVariable("typeId") Integer id,
                                               PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return substanceService.getObjectsByType(id, pageable);
    }

}
