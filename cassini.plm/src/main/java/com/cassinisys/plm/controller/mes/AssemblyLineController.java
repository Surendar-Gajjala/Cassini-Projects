package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.AssemblyLineCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mes.MESAssemblyLine;
import com.cassinisys.plm.model.mes.MESWorkCenter;
import com.cassinisys.plm.service.mes.AssemblyLineService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by GSR on 09-02-2021.
 */
@RestController
@RequestMapping("/mes/assemblylines")
@Api(tags = "PLM.MES", description = "MES Related")
public class AssemblyLineController extends BaseController {

    @Autowired
    private AssemblyLineService assemblyLineService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESAssemblyLine create(@RequestBody MESAssemblyLine assemblyLine) {
        return assemblyLineService.create(assemblyLine);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESAssemblyLine update(@PathVariable("id") Integer id,
                                  @RequestBody MESAssemblyLine assemblyLine) {
        return assemblyLineService.update(assemblyLine);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        assemblyLineService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESAssemblyLine get(@PathVariable("id") Integer id) {
        return assemblyLineService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESAssemblyLine> getAll() {
        return assemblyLineService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESAssemblyLine> getMultiple(@PathVariable Integer[] ids) {
        return assemblyLineService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MESAssemblyLine> getAllAssemblyLines(PageRequest pageRequest, AssemblyLineCriteria assemblyLineCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return assemblyLineService.getAllAssemblyLines(pageable, assemblyLineCriteria);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESAssemblyLine> getObjectsByType(@PathVariable("typeId") Integer id,
                                                  PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return assemblyLineService.getObjectsByType(id, pageable);
    }

    @RequestMapping(value = "/{id}/workcenters",method = RequestMethod.GET)
    public List<MESWorkCenter> getAssemblyLineWorkCenters(@PathVariable("id") Integer id) {
        return assemblyLineService.getAssemblyLineWorkCenters(id);
    }

    @RequestMapping(value = "/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getAssemblyLineTabCount(@PathVariable("id") Integer id) {
        return assemblyLineService.getAssemblyLineTabCount(id);
    }

}
