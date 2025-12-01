package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.MaterialCriteria;
import com.cassinisys.plm.model.mes.MESMaterial;
import com.cassinisys.plm.model.mes.dto.MaterialDto;
import com.cassinisys.plm.service.mes.MaterialService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CassiniSystems on 27-10-2020.
 */
@RestController
@RequestMapping("/mes/materials")
@Api(tags = "PLM.MES", description = "MES Related")
public class MaterialController extends BaseController {


    @Autowired
    private MaterialService materialService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESMaterial create(@RequestBody MESMaterial material) {
        return materialService.create(material);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESMaterial update(@PathVariable("id") Integer id,
                              @RequestBody MESMaterial material) {
        return materialService.update(material);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        materialService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESMaterial get(@PathVariable("id") Integer id) {
        return materialService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESMaterial> getAll() {
        return materialService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<MESMaterial> getMultiple(@PathVariable Integer[] ids) {
        return materialService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MaterialDto> getAllMaterials(PageRequest pageRequest, MaterialCriteria materialCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return materialService.getAllMaterials(pageable, materialCriteria);
    }

    @RequestMapping(value = "/filtered", method = RequestMethod.GET)
    public Page<MESMaterial> getAllFilteredMaterials(PageRequest pageRequest, MaterialCriteria materialCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return materialService.getAllFilteredMaterials(pageable, materialCriteria);
    }

    @RequestMapping(value = "/{id}/image", method = RequestMethod.POST)
    public MESMaterial uploadImage(@PathVariable("id") Integer id, MultipartHttpServletRequest request) {
        return materialService.uploadImage(id, request);
    }

    @RequestMapping(value = "/{id}/image/download", method = RequestMethod.GET)
    public void downloadImage(@PathVariable("id") Integer id,
                              HttpServletResponse response) {
        materialService.downloadImage(id, response);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESMaterial> getObjectsByType(@PathVariable("typeId") Integer id,
                                              PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return materialService.getObjectsByType(id, pageable);
    }

}
