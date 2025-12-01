package com.cassinisys.plm.controller.pqm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.NCRCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.NCRsDto;
import com.cassinisys.plm.service.pqm.NCRService;
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
@RequestMapping("/pqm/ncrs")
@Api(tags = "PLM.PQM",description = "Quality Related")
public class NCRController extends BaseController {

    @Autowired
    private NCRService ncrService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PQMNCR create(@RequestBody PQMNCR ncr) {
        return ncrService.create(ncr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PQMNCR update(@PathVariable("id") Integer id,
                         @RequestBody PQMNCR ncr) {
        return ncrService.update(ncr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        ncrService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PQMNCR get(@PathVariable("id") Integer id) {
        return ncrService.get(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public NCRsDto getNCRDetails(@PathVariable("id") Integer id) {
        return ncrService.getNCRDetails(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PQMNCR> getAll() {
        return ncrService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMNCR> getMultiple(@PathVariable Integer[] ids) {
        return ncrService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<NCRsDto> getAllNCRs(PageRequest pageRequest, NCRCriteria ncrCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ncrService.getAllNcrs(pageable, ncrCriteria);
    }

    @RequestMapping(value = "/{id}/files", method = RequestMethod.GET)
    public List<PQMNCRFile> getNcrFiles(@PathVariable("id") Integer id) {
        return ncrService.getNcrFiles(id);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PQMNCRAttribute createNcrAttribute(@PathVariable("id") Integer id,
                                              @RequestBody PQMNCRAttribute attribute) {
        return ncrService.createNcrAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PQMNCRAttribute updateNcrAttribute(@PathVariable("id") Integer id,
                                              @RequestBody PQMNCRAttribute attribute) {
        return ncrService.updateNcrAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/problemItems", method = RequestMethod.POST)
    public PQMNCRProblemItem createProblemItem(@PathVariable("id") Integer id,
                                               @RequestBody PQMNCRProblemItem problemItem) {
        return ncrService.createProblemItem(id, problemItem);
    }

    @RequestMapping(value = "/{id}/problemItems/{itemId}", method = RequestMethod.PUT)
    public PQMNCRProblemItem updateProblemItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId,
                                               @RequestBody PQMNCRProblemItem problemItem) {
        return ncrService.updateProblemItem(id, problemItem);
    }

    @RequestMapping(value = "/{id}/problemItems/multiple", method = RequestMethod.POST)
    public List<PQMNCRProblemItem> createProblemItems(@PathVariable("id") Integer id,
                                                      @RequestBody List<PQMNCRProblemItem> problemItem) {
        return ncrService.createProblemItems(id, problemItem);
    }

    @RequestMapping(value = "/{id}/problemItems", method = RequestMethod.GET)
    public List<PQMNCRProblemItem> getProblemItems(@PathVariable("id") Integer id) {
        return ncrService.getProblemItems(id);
    }

    @RequestMapping(value = "/{id}/problemItems/{itemId}", method = RequestMethod.DELETE)
    public void deleteProblemItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        ncrService.deleteProblemItem(id, itemId);
    }


    @RequestMapping(value = "/{id}/relatedItems", method = RequestMethod.POST)
    public PQMNCRRelatedItem createRelatedItem(@PathVariable("id") Integer id,
                                               @RequestBody PQMNCRRelatedItem problemItem) {
        return ncrService.createRelatedItem(id, problemItem);
    }

    @RequestMapping(value = "/{id}/relatedItems/{itemId}", method = RequestMethod.PUT)
    public PQMNCRRelatedItem updateRelatedItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId,
                                               @RequestBody PQMNCRRelatedItem problemItem) {
        return ncrService.updateRelatedItem(id, problemItem);
    }

    @RequestMapping(value = "/{id}/relatedItems/multiple", method = RequestMethod.POST)
    public List<PQMNCRRelatedItem> createRelatedItems(@PathVariable("id") Integer id,
                                                      @RequestBody List<PQMNCRRelatedItem> problemItem) {
        return ncrService.createRelatedItems(id, problemItem);
    }

    @RequestMapping(value = "/{id}/relatedItems", method = RequestMethod.GET)
    public List<PQMNCRRelatedItem> getRelatedItems(@PathVariable("id") Integer id) {
        return ncrService.getRelatedItems(id);
    }

    @RequestMapping(value = "/{id}/relatedItems/{itemId}", method = RequestMethod.DELETE)
    public void deleteRelatedItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        ncrService.deleteRelatedItem(id, itemId);
    }

    @RequestMapping(value = "{id}/details/count", method = RequestMethod.GET)
    public ItemDetailsDto getDetailsCount(@PathVariable("id") Integer id) {
        return ncrService.getDetailsCount(id);
    }
}
