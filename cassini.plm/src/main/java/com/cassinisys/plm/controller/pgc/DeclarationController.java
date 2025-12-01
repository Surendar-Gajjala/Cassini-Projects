package com.cassinisys.plm.controller.pgc;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.DeclarationCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.pgc.PGCBosItem;
import com.cassinisys.plm.model.pgc.PGCDeclaration;
import com.cassinisys.plm.model.pgc.PGCDeclarationPart;
import com.cassinisys.plm.model.pgc.PGCDeclarationSpecification;
import com.cassinisys.plm.service.pgc.DeclarationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Lenovo on 27-11-2020.
 */
@RestController
@RequestMapping("/pgc/declarations")
@Api(tags = "PLM.PGC", description = "PGC Related")
public class DeclarationController extends BaseController {

    @Autowired
    private DeclarationService declarationService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PGCDeclaration create(@RequestBody PGCDeclaration declaration) {
        return declarationService.create(declaration);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PGCDeclaration update(@PathVariable("id") Integer id,
                                 @RequestBody PGCDeclaration declaration) {
        return declarationService.update(declaration);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        declarationService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PGCDeclaration get(@PathVariable("id") Integer id) {
        return declarationService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PGCDeclaration> getAll() {
        return declarationService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PGCDeclaration> getMultiple(@PathVariable Integer[] ids) {
        return declarationService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<PGCDeclaration> getAllDeclarations(PageRequest pageRequest, DeclarationCriteria declarationCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return declarationService.getAllDeclarations(pageable, declarationCriteria);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<PGCDeclaration> getObjectsByType(@PathVariable("typeId") Integer id,
                                                 PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return declarationService.getObjectsByType(id, pageable);
    }

    @RequestMapping(value = "/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getDeclarationTabCounts(@PathVariable("id") Integer id) {
        return declarationService.getDeclarationTabCounts(id);
    }

    @RequestMapping(value = "/{decId}/parts", method = RequestMethod.POST)
    public PGCDeclarationPart createDeclarationPart(@PathVariable("decId") Integer decId,
                                                    @RequestBody PGCDeclarationPart declarationPart) {
        return declarationService.createDeclarationPart(decId, declarationPart);
    }

    @RequestMapping(value = "/{decId}/parts/{partId}", method = RequestMethod.PUT)
    public PGCDeclarationPart updateDeclarationPart(@PathVariable("decId") Integer decId, @PathVariable("partId") Integer partId,
                                                    @RequestBody PGCDeclarationPart declarationPart) {
        return declarationService.updateDeclarationPart(decId, declarationPart);
    }

    @RequestMapping(value = "/{decId}/parts/multiple", method = RequestMethod.POST)
    public List<PGCDeclarationPart> createDeclarationParts(@PathVariable("decId") Integer decId,
                                                           @RequestBody List<PGCDeclarationPart> declarationParts) {
        return declarationService.createDeclarationParts(decId, declarationParts);
    }

    @RequestMapping(value = "/{decId}/parts", method = RequestMethod.GET)
    public List<PGCDeclarationPart> getAllDeclarationParts(@PathVariable("decId") Integer decId) {
        return declarationService.getAllDeclarationParts(decId);
    }

    @RequestMapping(value = "/{decId}/parts/{partId}", method = RequestMethod.DELETE)
    public void deleteDeclarationPart(@PathVariable("decId") Integer decId, @PathVariable("partId") Integer partId) {
        declarationService.deleteDeclarationPart(decId, partId);
    }

    @RequestMapping(value = "/{decId}/submit", method = RequestMethod.GET)
    public PGCDeclaration submitDeclarationToContactPerson(@PathVariable("decId") Integer decId) {
        return declarationService.submitDeclarationToContactPerson(decId);
    }


    @RequestMapping(value = "/parts/{partId}/substances/multiple", method = RequestMethod.POST)
    public List<PGCBosItem> createMultipleBOSItem(@PathVariable("partId") Integer partId,
                                                  @RequestBody List<PGCBosItem> bosItems) {
        return declarationService.createMultipleBOSItem(partId, bosItems);
    }

    @RequestMapping(value = "/parts/{partId}/substances", method = RequestMethod.POST)
    public PGCBosItem createBOSItem(@PathVariable("partId") Integer partId,
                                    @RequestBody PGCBosItem pgcBosItem) {
        return declarationService.createBOSItem(partId, pgcBosItem);
    }

    @RequestMapping(value = "/parts/{partId}/substances/{substanceId}", method = RequestMethod.PUT)
    public PGCBosItem updateBOSItem(@PathVariable("partId") Integer partId, @PathVariable("substanceId") Integer substanceId,
                                    @RequestBody PGCBosItem pgcBosItem) {
        return declarationService.updateBOSItem(partId, pgcBosItem);
    }

    @RequestMapping(value = "/parts/{partId}/substances/{substanceId}", method = RequestMethod.DELETE)
    public void deleteBOSItem(@PathVariable("partId") Integer partId, @PathVariable("substanceId") Integer substanceId) {
        declarationService.deleteBOSItem(partId, substanceId);
    }

    @RequestMapping(value = "/{decId}/specifications/multiple", method = RequestMethod.POST)
    public List<PGCDeclarationSpecification> createDeclarationSpecifications(@PathVariable("decId") Integer decId,
                                                                             @RequestBody List<PGCDeclarationSpecification> declarationSpecifications) {
        return declarationService.createDeclarationSpecifications(decId, declarationSpecifications);
    }

    @RequestMapping(value = "/{decId}/specifications", method = RequestMethod.GET)
    public List<PGCDeclarationSpecification> getDeclarationSpecifications(@PathVariable("decId") Integer decId) {
        return declarationService.getDeclarationSpecifications(decId);
    }

    @RequestMapping(value = "/{decId}/specifications/{specId}", method = RequestMethod.DELETE)
    public void deleteDeclarationSpecification(@PathVariable("decId") Integer decId, @PathVariable("specId") Integer specId) {
        declarationService.deleteDeclarationSpecification(decId, specId);
    }

    @RequestMapping(value = "/{declarationId}/compliancereport", method = RequestMethod.GET)
    public PGCDeclaration generateComplianceReport(@PathVariable("declarationId") Integer declarationId) {
        return declarationService.generateComplianceReport(declarationId);
    }
}
