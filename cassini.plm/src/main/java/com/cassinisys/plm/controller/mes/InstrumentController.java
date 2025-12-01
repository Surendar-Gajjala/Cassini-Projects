package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.InstrumentCriteria;
import com.cassinisys.plm.model.mes.MESInstrument;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.service.mes.InstrumentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/mes/instruments")
@Api(tags = "PLM.MES", description = "MES Related")
public class InstrumentController extends BaseController {
    @Autowired
    private InstrumentService instrumentService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESInstrument create(@RequestBody MESInstrument instrument) {
        return instrumentService.create(instrument);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESInstrument update(@PathVariable("id") Integer id,
                                @RequestBody MESInstrument instrument) {
        return instrumentService.update(instrument);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        instrumentService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESInstrument get(@PathVariable("id") Integer id) {
        return instrumentService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESInstrument> getAll() {
        return instrumentService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MESInstrument> getAllInstruments(PageRequest pageRequest, InstrumentCriteria instrumentCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return instrumentService.getAllInstrumentsByPageable(pageable, instrumentCriteria);
    }

    @RequestMapping(value = "/{instrumentId}/image", method = RequestMethod.POST)
    public MESInstrument uploadImage(@PathVariable("instrumentId") Integer instrumentId, MultipartHttpServletRequest request) {
        return instrumentService.uploadImage(instrumentId, request);
    }

    @RequestMapping(value = "/{instrumentId}/image/download", method = RequestMethod.GET)
    public void downloadImage(@PathVariable("instrumentId") Integer instrumentId,
                              HttpServletResponse response) {
        instrumentService.downloadImage(instrumentId, response);
    }

    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public void saveInstrumentAttributes(@PathVariable("id") Integer id,
                                         @RequestBody List<MESObjectAttribute> attributes) {
        instrumentService.saveInstrumentAttributes(attributes);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public MESObjectAttribute updateInstrumentAttribute(@PathVariable("id") Integer id,
                                                        @RequestBody MESObjectAttribute attribute) {
        return instrumentService.updateInstrumentAttribute(attribute);
    }


    @RequestMapping(value = "/uploadimageattribute/{objectId}/{attributeId}", method = RequestMethod.POST)
    public MESInstrument saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                                 @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return instrumentService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESInstrument> getInstrumentsByType(@PathVariable("typeId") Integer id,
                                                    PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return instrumentService.getInstrumentsByType(id, pageable);
    }

}