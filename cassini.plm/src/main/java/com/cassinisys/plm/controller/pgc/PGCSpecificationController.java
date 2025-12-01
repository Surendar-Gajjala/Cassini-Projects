package com.cassinisys.plm.controller.pgc;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.PGCSpecificationCriteria;
import com.cassinisys.plm.filtering.SpecificationSubstanceCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.pgc.PGCSpecification;
import com.cassinisys.plm.model.pgc.PGCSpecificationSubstance;
import com.cassinisys.plm.model.pgc.PGCSubstance;
import com.cassinisys.plm.service.pgc.PGCSpecificationService;
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
@RequestMapping("/pgc/specifications")
@Api(tags = "PLM.PGC", description = "PGC Related")
public class
        PGCSpecificationController extends BaseController {


    @Autowired
    private PGCSpecificationService pgcSpecificationService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PGCSpecification create(@RequestBody PGCSpecification specification) {
        return pgcSpecificationService.create(specification);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PGCSpecification update(@PathVariable("id") Integer id,
                                   @RequestBody PGCSpecification specification) {
        return pgcSpecificationService.update(specification);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        pgcSpecificationService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PGCSpecification get(@PathVariable("id") Integer id) {
        return pgcSpecificationService.get(id);
    }

    @RequestMapping(value = "/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getTabCounts(@PathVariable("id") Integer id) {
        return pgcSpecificationService.getTabCounts(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PGCSpecification> getAll() {
        return pgcSpecificationService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PGCSpecification> getMultiple(@PathVariable Integer[] ids) {
        return pgcSpecificationService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<PGCSpecification> getAllSpecifications(PageRequest pageRequest, PGCSpecificationCriteria specificationCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pgcSpecificationService.getAllSpecifications(pageable, specificationCriteria);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<PGCSpecification> getObjectsByType(@PathVariable("typeId") Integer id,
                                                   PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pgcSpecificationService.getObjectsByType(id, pageable);
    }

    @RequestMapping(value = "/allsubstances", method = RequestMethod.GET)
    public Page<PGCSubstance> getAllSubstances(PageRequest pageRequest, SpecificationSubstanceCriteria specificationSubstanceCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pgcSpecificationService.getAllSubstances(pageable, specificationSubstanceCriteria);
    }

    @RequestMapping(value = "/{specId}/substance", method = RequestMethod.POST)
    public PGCSpecificationSubstance createSpecSubstance(@PathVariable("specId") Integer specId,
                                                         @RequestBody PGCSpecificationSubstance specificationSubstance) {
        return pgcSpecificationService.createSpecSubstance(specId, specificationSubstance);
    }

    @RequestMapping(value = "/{specId}/substance/{subId}", method = RequestMethod.PUT)
    public PGCSpecificationSubstance updateSpecSubstance(@PathVariable("specId") Integer specId, @PathVariable("subId") Integer subId,
                                                         @RequestBody PGCSpecificationSubstance specificationSubstance) {
        return pgcSpecificationService.updateSpecSubstance(specId, specificationSubstance);
    }

    @RequestMapping(value = "/{specId}/substance/multiple", method = RequestMethod.POST)
    public List<PGCSpecificationSubstance> createSpecSubstances(@PathVariable("specId") Integer specId,
                                                                @RequestBody List<PGCSpecificationSubstance> specificationSubstances) {
        return pgcSpecificationService.createSpecSubstances(specId, specificationSubstances);
    }

    @RequestMapping(value = "/{specId}/specsubstances", method = RequestMethod.GET)
    public List<PGCSpecificationSubstance> getAllSpecSubstances(@PathVariable("specId") Integer specId) {
        return pgcSpecificationService.getAllSpecSubstances(specId);
    }

    @RequestMapping(value = "/{specId}/specsubstance/{specSubId}", method = RequestMethod.DELETE)
    public void deleteSpecSubstance(@PathVariable("specId") Integer specId, @PathVariable("specSubId") Integer specSubId) {
        pgcSpecificationService.deleteSpecSubstance(specId, specSubId);
    }

}
