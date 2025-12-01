package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.plm.filtering.LifeCycleCriteria;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.service.plm.LifeCycleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyamreddy on 018 18-May -17.
 */
@RestController
@RequestMapping("/plm/lifecycles")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class LifeCycleController extends BaseController {

    @Autowired
    private LifeCycleService lifeCycleService;

    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(method = RequestMethod.POST)
    public PLMLifeCycle create(@RequestBody PLMLifeCycle lifeCycle) {
        return lifeCycleService.create(lifeCycle);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMLifeCycle> getAll() {
        return lifeCycleService.getAll();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<PLMLifeCycle> search(LifeCycleCriteria criteria) {
        return lifeCycleService.find(criteria);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMLifeCycle getLifecycle(@PathVariable("id") Integer id) {
        return lifeCycleService.get(id);
    }

    @RequestMapping(value = "/{lcId}", method = RequestMethod.PUT)
    public PLMLifeCycle updateLifecycle(@RequestBody PLMLifeCycle lc) {
        return lifeCycleService.update(lc);
    }

    @RequestMapping(value = "/{lcId}", method = RequestMethod.DELETE)
    public void deleteLifecycle(@PathVariable("lcId") Integer lcId) {
        PLMLifeCycle lc = lifeCycleService.get(lcId);
        if (lc != null) {
            List<PLMItemType> types = itemTypeRepository.findByLifecycle(lc);
            if (types.size() > 0) {
                throw new CassiniException(messageSource.getMessage("lifecycle_referenced_in_one_or_more_itemType",
                        null, "This lifecycle is referenced in one or more item types. Cannot delete this lifecycle.", LocaleContextHolder.getLocale()));
            }
            lc.getPhases().forEach(p -> lifeCycleService.deletePhase(p.getId()));
            lifeCycleService.delete(lcId);
        }
    }

    @RequestMapping(value = "/{id}/phases", method = RequestMethod.GET)
    public List<PLMLifeCyclePhase> getPhases(@PathVariable("id") Integer id) {
        return lifeCycleService.getPhases(id);
    }

    @RequestMapping(value = "/phases", method = RequestMethod.GET)
    public List<PLMLifeCyclePhase> getAllPhases() {
        return lifeCycleService.getAllPhases();
    }

    @RequestMapping(value = "/allPhases", method = RequestMethod.GET)
    public List<PLMLifeCyclePhase> getAllLifeCyclePhases() {
        return lifeCycleService.getAllLifeCyclePhases();
    }

    @RequestMapping(value = "/{id}/phases", method = RequestMethod.POST)
    public PLMLifeCyclePhase createPhase(@RequestBody PLMLifeCyclePhase phase) {
        return lifeCycleService.createPhase(phase);
    }

    @RequestMapping(value = "/{lcId}/phases/{phaseId}", method = RequestMethod.PUT)
    public PLMLifeCyclePhase updatePhase(@RequestBody PLMLifeCyclePhase phase) {
        return lifeCycleService.updatePhase(phase);
    }

    @RequestMapping(value = "/{lcId}/phases/{phaseId}", method = RequestMethod.DELETE)
    public void deletePhase(@PathVariable("phaseId") Integer phaseId) {
        lifeCycleService.deletePhase(phaseId);
    }

}
