package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.model.PDMLockable;
import com.cassinisys.pdm.service.PDMLockableService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by GSR on 15-02-2017.
 */
@RestController
@RequestMapping("pdm/lockable")
public class PDMLockableController extends BaseController {

    @Autowired
    PageRequestConverter pageRequestConverter;
    @Autowired
    private PDMLockableService lockableService;

    @RequestMapping(method = RequestMethod.POST)
    public PDMLockable create(@RequestBody PDMLockable pdmLockable) {
        pdmLockable.setId(null);
        return lockableService.create(pdmLockable);
    }

    @RequestMapping(value = "/{lockableId}", method = RequestMethod.PUT)
    public PDMLockable update(@PathVariable("lockableId") Integer lockableId, @RequestBody PDMLockable pdmLockable) {
        pdmLockable.setId(lockableId);
        return lockableService.update(pdmLockable);
    }

    @RequestMapping(value = "/{lockableId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("lockableId") Integer lockableId) {
        lockableService.delete(lockableId);
    }

    @RequestMapping(value = "/{lockableId}", method = RequestMethod.GET)
    public PDMLockable get(@PathVariable("lockableId") Integer lockableId) {
        return lockableService.get(lockableId);
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public List<PDMLockable> getAll() {
        return lockableService.getAll();

    }
}
