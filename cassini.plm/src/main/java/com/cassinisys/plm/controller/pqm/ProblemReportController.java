package com.cassinisys.plm.controller.pqm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ProblemReportCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.PRItemsDto;
import com.cassinisys.plm.model.pqm.dto.ProblemReportsDto;
import com.cassinisys.plm.service.pqm.ProblemReportService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by subramanyam on 04-06-2020.
 */
@RestController
@RequestMapping("/pqm/problemreports")
@Api(tags = "PLM.PQM",description = "Quality Related")
public class ProblemReportController extends BaseController {

    @Autowired
    private ProblemReportService problemReportService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PQMProblemReport create(@RequestBody PQMProblemReport problemReport) {
        return problemReportService.create(problemReport);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PQMProblemReport update(@PathVariable("id") Integer id,
                                   @RequestBody PQMProblemReport problemReport) {
        return problemReportService.update(problemReport);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long getPRCount() {
        return problemReportService.getPRCount();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        problemReportService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PQMProblemReport get(@PathVariable("id") Integer id) {
        return problemReportService.get(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public ProblemReportsDto getProblemReportDetails(@PathVariable("id") Integer id) {
        return problemReportService.getProblemReportDetails(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PQMProblemReport> getAll() {
        return problemReportService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMProblemReport> getMultiple(@PathVariable Integer[] ids) {
        return problemReportService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<ProblemReportsDto> getAllProblemReports(PageRequest pageRequest, ProblemReportCriteria problemReportCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return problemReportService.getAllProblemReports(pageable, problemReportCriteria);
    }

    @RequestMapping(value = "/list/released", method = RequestMethod.GET)
    public List<PQMProblemReport> getReleasedProblemReports() {
        return problemReportService.getReleasedProblemReports();
    }

    @RequestMapping(value = "/{id}/files", method = RequestMethod.GET)
    public List<PQMProblemReportFile> getPrFiles(@PathVariable("id") Integer id) {
        return problemReportService.getPrFiles(id);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PQMProblemReportAttribute createPrAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody PQMProblemReportAttribute attribute) {
        return problemReportService.createPrAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PQMProblemReportAttribute updatePrAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody PQMProblemReportAttribute attribute) {
        return problemReportService.updatePrAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/problemItems", method = RequestMethod.POST)
    public PQMPRProblemItem createProblemItem(@PathVariable("id") Integer id,
                                              @RequestBody PQMPRProblemItem problemItem) {
        return problemReportService.createProblemItem(id, problemItem);
    }

    @RequestMapping(value = "/{id}/problemItems/{itemId}", method = RequestMethod.PUT)
    public PQMPRProblemItem updateProblemItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId,
                                              @RequestBody PQMPRProblemItem problemItem) {
        return problemReportService.updateProblemItem(id, problemItem);
    }

    @RequestMapping(value = "/{id}/problemItems/multiple", method = RequestMethod.POST)
    public List<PQMPRProblemItem> createProblemItems(@PathVariable("id") Integer id,
                                                     @RequestBody List<PQMPRProblemItem> problemItem) {
        return problemReportService.createProblemItems(id, problemItem);
    }

    @RequestMapping(value = "/{id}/problemItems", method = RequestMethod.GET)
    public List<PRItemsDto> getProblemItems(@PathVariable("id") Integer id) {
        return problemReportService.getProblemItems(id);
    }

    @RequestMapping(value = "/{id}/problemItems/{itemId}", method = RequestMethod.DELETE)
    public void deleteProblemItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        problemReportService.deleteProblemItem(id, itemId);
    }


    @RequestMapping(value = "/{id}/relatedItems", method = RequestMethod.POST)
    public PQMPRRelatedItem createRelatedItem(@PathVariable("id") Integer id,
                                              @RequestBody PQMPRRelatedItem problemItem) {
        return problemReportService.createRelatedItem(id, problemItem);
    }

    @RequestMapping(value = "/{id}/relatedItems/multiple", method = RequestMethod.POST)
    public List<PQMPRRelatedItem> createRelatedItems(@PathVariable("id") Integer id,
                                                     @RequestBody List<PQMPRRelatedItem> problemItem) {
        return problemReportService.createRelatedItems(id, problemItem);
    }

    @RequestMapping(value = "/{id}/relatedItems", method = RequestMethod.GET)
    public List<PRItemsDto> getRelatedItems(@PathVariable("id") Integer id) {
        return problemReportService.getRelatedItems(id);
    }

    @RequestMapping(value = "/{id}/relatedItems/{itemId}", method = RequestMethod.DELETE)
    public void deleteRelatedItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        problemReportService.deleteRelatedItem(id, itemId);
    }

    @RequestMapping(value = "{id}/details/count", method = RequestMethod.GET)
    public ItemDetailsDto getDetailsCount(@PathVariable("id") Integer id) {
        return problemReportService.getDetailsCount(id);
    }
}
