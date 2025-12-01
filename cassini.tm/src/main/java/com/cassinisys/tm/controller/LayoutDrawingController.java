package com.cassinisys.tm.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.tm.model.TMLayoutDrawing;
import com.cassinisys.tm.service.LayoutDrawingService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by CassiniSystems on 07-07-2016.
 */
@Api(name = "LayoutDrawing",
        description = "LayoutDrawing endpoint")
@RestController
@RequestMapping("/layoutDrawings")
public class LayoutDrawingController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private LayoutDrawingService layoutDrawingService;

    @RequestMapping(method = RequestMethod.POST)
    public TMLayoutDrawing create(@RequestBody TMLayoutDrawing layoutDrawing) {

        return layoutDrawingService.create(layoutDrawing);
    }

    @RequestMapping(value = "/{layoutDrawingId}", method = RequestMethod.PUT)
    public TMLayoutDrawing update(@PathVariable("layoutDrawingId") Integer layoutDrawingId,
                                  @RequestBody TMLayoutDrawing layoutDrawing) {
        layoutDrawing.setId(layoutDrawingId);
        return layoutDrawingService.update(layoutDrawing);
    }

    @RequestMapping(value = "/{layoutDrawingId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("layoutDrawingId") Integer layoutDrawingId) {
        layoutDrawingService.delete(layoutDrawingId);
    }

    @RequestMapping(value = "/{layoutDrawingId}", method = RequestMethod.GET)
    public TMLayoutDrawing get(@PathVariable("layoutDrawingId") Integer layoutDrawingId) {
        return layoutDrawingService.get(layoutDrawingId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<TMLayoutDrawing> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return layoutDrawingService.findAll(pageable);
    }

    @RequestMapping(value = "/bydate/{date}",method = RequestMethod.GET)
    public List<TMLayoutDrawing> getAll(@PathVariable("date") String date) {
        return layoutDrawingService.findByDate(date);
    }


}
