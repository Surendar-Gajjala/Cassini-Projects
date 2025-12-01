package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ToolCriteria;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESTool;
import com.cassinisys.plm.service.mes.ToolService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Suresh CassiniPLM on 27-10-2020.
 */
@RestController
@RequestMapping("/mes/tools")
@Api(tags = "PLM.MES", description = "MES Related")
public class ToolController extends BaseController {
    @Autowired
    private ToolService toolService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESTool create(@RequestBody MESTool tool) {
        return toolService.create(tool);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESTool update(@PathVariable("id") Integer id,
                          @RequestBody MESTool tool) {
        return toolService.update(tool);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        toolService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESTool get(@PathVariable("id") Integer id) {
        return toolService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESTool> getAll() {
        return toolService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MESTool> getAllTools(PageRequest pageRequest, ToolCriteria toolCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return toolService.getAllToolsByPageable(pageable, toolCriteria);
    }

    @RequestMapping(value = "/{toolId}/image", method = RequestMethod.POST)
    public MESTool uploadImage(@PathVariable("toolId") Integer toolId, MultipartHttpServletRequest request) {
        return toolService.uploadImage(toolId, request);
    }

    @RequestMapping(value = "/{toolId}/image/download", method = RequestMethod.GET)
    public void downloadImage(@PathVariable("toolId") Integer toolId,
                              HttpServletResponse response) {
        toolService.downloadImage(toolId, response);
    }

    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public void saveToolAttributes(@PathVariable("id") Integer id,
                                   @RequestBody List<MESObjectAttribute> attributes) {
        toolService.saveToolAttributes(attributes);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public MESObjectAttribute updateToolAttribute(@PathVariable("id") Integer id,
                                                  @RequestBody MESObjectAttribute attribute) {
        return toolService.updateToolAttribute(attribute);
    }


    @RequestMapping(value = "/uploadimageattribute/{objectId}/{attributeId}", method = RequestMethod.POST)
    public MESTool saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                           @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return toolService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESTool> getToolsByType(@PathVariable("typeId") Integer id,
                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return toolService.getToolsByType(id, pageable);
    }

}