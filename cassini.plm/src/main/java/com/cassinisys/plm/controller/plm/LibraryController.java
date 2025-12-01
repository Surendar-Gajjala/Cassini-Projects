package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.plm.PLMLibrary;
import com.cassinisys.plm.service.plm.LibraryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@RestController
@RequestMapping("/plm/libraries")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class LibraryController extends BaseController {

    @Autowired
    private LibraryService libraryService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMLibrary creteLibrary(@RequestBody PLMLibrary PLMLibrary) {
        return libraryService.create(PLMLibrary);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public PLMLibrary updateLibrary(@RequestBody PLMLibrary PLMLibrary) {
        return libraryService.update(PLMLibrary);
    }

    @RequestMapping(value = "/{libraryId}", method = RequestMethod.DELETE)
    public void deleteLibrary(@PathVariable("libraryId") Integer libraryId) {
        libraryService.delete(libraryId);
    }

    @RequestMapping(value = "/{libraryId}", method = RequestMethod.GET)
    public PLMLibrary getLibrary(@PathVariable("libraryId") Integer libraryId) {
        return libraryService.get(libraryId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMLibrary> getLibraries() {
        return libraryService.getAll();
    }
}
