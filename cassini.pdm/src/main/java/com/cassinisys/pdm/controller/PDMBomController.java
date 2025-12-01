package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.model.PDMBom;
import com.cassinisys.pdm.service.PDMBomService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Api(name = "BOM", description = "BOM endpoint", group = "PDM")
@RestController
@RequestMapping("pdm/boms")
public class PDMBomController extends BaseController{

    @Autowired
    private PDMBomService pdmBomService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PDMBom create(@RequestBody PDMBom pdmBom){
        pdmBom.setId(null);
        return pdmBomService.create(pdmBom);
    }

    @RequestMapping(value = "/{bomId}",method = RequestMethod.PUT)
    public PDMBom update(@PathVariable("bomId") Integer bomId, @RequestBody PDMBom pdmBom){
        pdmBom.setId(bomId);
        return pdmBomService.update(pdmBom);
    }

    @RequestMapping(value = "/{bomId}",method = RequestMethod.DELETE)
    public void delete(@PathVariable("bomId") Integer bomId){
        pdmBomService.delete(bomId);
    }

    @RequestMapping(value = "/{bomId}",method = RequestMethod.GET)
    public PDMBom get(@PathVariable("bomId") Integer bomId){
        return pdmBomService.get(bomId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PDMBom> getAll(){
        return pdmBomService.getAll();
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public Page<PDMBom> findAll(PageRequest pageRequest){
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pdmBomService.findAll(pageable);
    }
}
