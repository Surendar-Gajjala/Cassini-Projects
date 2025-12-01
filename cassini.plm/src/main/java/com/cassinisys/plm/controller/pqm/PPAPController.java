package com.cassinisys.plm.controller.pqm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.PPAPCriteria;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.pqm.PQMPPAP;
import com.cassinisys.plm.model.pqm.PQMPPAPAttribute;
import com.cassinisys.plm.service.pqm.PPAPService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pqm/ppap")
@Api(tags = "PLM.PQM", description = "Quality Related")
public class PPAPController extends BaseController {

    @Autowired
    private PPAPService ppapService;

    @Autowired
    private PageRequestConverter pageRequestConverter;


    @RequestMapping(method = RequestMethod.POST)
    private PQMPPAP create(@RequestBody PQMPPAP ppap) {
        return ppapService.create(ppap);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    private PQMPPAP update(@PathVariable("id") Integer id, @RequestBody PQMPPAP ppap) {
        return ppapService.update(ppap);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    private void delete(@PathVariable("id") Integer id) {
        ppapService.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    private List<PQMPPAP> getAll() {
        return ppapService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<PQMPPAP> getAllPPAPs(PageRequest pageRequest, PPAPCriteria ppapCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ppapService.getAllPPAPsByPageable(pageable, ppapCriteria);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PQMPPAPAttribute createPPAPAttribute(@PathVariable("id") Integer id,
                                                @RequestBody PQMPPAPAttribute attribute) {
        return ppapService.createPPAPAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PQMPPAPAttribute updatePPAPAttribute(@PathVariable("id") Integer id,
                                                @RequestBody PQMPPAPAttribute attribute) {
        return ppapService.updatePPAPAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/counts", method = RequestMethod.GET)
    public DetailsCount getPPAPTabCounts(@PathVariable Integer id) {
        return ppapService.getPPAPTabCounts(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private PQMPPAP get(@PathVariable("id") Integer id) {
        return ppapService.getById(id);
    }

        /*
     * Promote and Demote Supplier
     */

    @RequestMapping(value = "/{id}/promote", method = RequestMethod.PUT)
    public PQMPPAP promotePpap(@PathVariable("id") Integer id, @RequestBody PQMPPAP ppap) {
        return ppapService.promotePpap(id, ppap);
    }

    @RequestMapping(value = "/{id}/demote", method = RequestMethod.PUT)
    public PQMPPAP demotePpap(@PathVariable("id") Integer id, @RequestBody PQMPPAP ppap) {
        return ppapService.demotePpap(id, ppap);
    }


}
