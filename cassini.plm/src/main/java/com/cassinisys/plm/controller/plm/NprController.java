package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.NprCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.plm.PLMNpr;
import com.cassinisys.plm.model.plm.PLMNprItem;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.plm.NprService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by GSR on 18-12-2020.
 */
@RestController
@RequestMapping("/plm/nprs")
@Api(tags = "PLM.PGC", description = "PGC Related")
public class NprController extends BaseController {


    @Autowired
    private NprService nprService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PLMNpr create(@RequestBody PLMNpr npr) {
        return nprService.create(npr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMNpr update(@PathVariable("id") Integer id,
                         @RequestBody PLMNpr npr) {
        return nprService.update(npr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        nprService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMNpr get(@PathVariable("id") Integer id) {
        return nprService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMNpr> getAll() {
        return nprService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMNpr> getMultiple(@PathVariable Integer[] ids) {
        return nprService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<PLMNpr> getAllNprs(PageRequest pageRequest, NprCriteria nprCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return nprService.getAllNprs(pageable, nprCriteria);
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public List<PLMNprItem> getNPRItems(@PathVariable("id") Integer id) {
        return nprService.getNPRItems(id);
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.POST)
    public PLMNprItem createNprItem(@PathVariable("id") Integer id, @RequestBody PLMNprItem nprItem) {
        return nprService.createNprItem(id, nprItem);
    }

    @RequestMapping(value = "/{id}/items/{itemId}", method = RequestMethod.PUT)
    public PLMNprItem updateNprItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId,
                                    @RequestBody PLMNprItem nprItem) {
        return nprService.updateNprItem(id,itemId,nprItem);
    }

    @RequestMapping(value = "/{id}/items/{itemId}", method = RequestMethod.DELETE)
    public void deleteNprItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        nprService.deleteNprItem(id, itemId);
    }

    @RequestMapping(value = "/{id}/submit", method = RequestMethod.GET)
    public PLMNpr submitNpr(@PathVariable("id") Integer id) {
        return nprService.submitNpr(id);
    }

    @RequestMapping(value = "/{id}/counts", method = RequestMethod.GET)
    public ItemDetailsDto getNprTabCounts(@PathVariable("id") Integer id) {
        return nprService.getNprTabCounts(id);
    }

    @RequestMapping(value = "/{id}/approve", method = RequestMethod.GET)
    public PLMNpr approveNpr(@PathVariable("id") Integer id) {
        return nprService.approveNpr(id);
    }

    @RequestMapping(value = "/{id}/reject", method = RequestMethod.PUT)
    public PLMNpr rejectNpr(@PathVariable("id") Integer id,
                            @RequestBody PLMNpr npr) {
        return nprService.rejectNpr(npr);
    }

    @RequestMapping(value = "/{id}/items/{itemId}/assignnumber", method = RequestMethod.GET)
    public PLMNprItem assignItemNumber(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        return nprService.assignItemNumber(id, itemId);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return nprService.attachNprWorkflow(id, wfDefId);
    }
}
