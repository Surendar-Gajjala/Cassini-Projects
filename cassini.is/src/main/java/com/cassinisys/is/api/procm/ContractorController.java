package com.cassinisys.is.api.procm;

import com.cassinisys.is.filtering.ContractorCriteria;
import com.cassinisys.is.model.procm.ISContractor;
import com.cassinisys.is.service.procm.ContractorService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by swapna on 21/01/19.
 */
@RestController
@RequestMapping("is/contracts/contractors")
public class ContractorController extends BaseController {

    @Autowired
    private ContractorService contractorService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ISContractor create(@RequestBody ISContractor contractor) {
        return contractorService.create(contractor);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISContractor> getAll() {
        return contractorService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISContractor> getByIds(@PathVariable("ids") List<Integer> ids) {
        return contractorService.getByIds(ids);
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISContractor> getPageableContractors(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return contractorService.getPageableContractors(pageable);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    List<ISContractor> getActiveContractors() {
        return contractorService.getActiveContractors();
    }

    @RequestMapping(value = "/{contractorId}", method = RequestMethod.GET)
    public ISContractor get(@PathVariable("contractorId") Integer contractorId) {
        return contractorService.get(contractorId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ISContractor update(@RequestBody ISContractor contractor) {
        return contractorService.update(contractor);
    }

    @RequestMapping(value = "/{contractorId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("contractorId") Integer contractorId) {
        contractorService.delete(contractorId);
    }

    @RequestMapping(value = "/contactPerson/{contactPerson}", method = RequestMethod.GET)
    public List<ISContractor> findByContactPerson(@PathVariable("contactPerson") Integer contactPerson) {
        return contractorService.findByContactPerson(contactPerson);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<ISContractor> contractorsFreeTextSearch(PageRequest pageRequest, ContractorCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return contractorService.contractorsFreeTextSearch(pageable, criteria);
    }
}
