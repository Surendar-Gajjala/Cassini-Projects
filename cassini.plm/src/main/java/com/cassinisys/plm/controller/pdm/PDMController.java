package com.cassinisys.plm.controller.pdm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.PDMObjectCriteria;
import com.cassinisys.plm.model.pdm.*;
import com.cassinisys.plm.model.pdm.dto.AssemblyDTO;
import com.cassinisys.plm.model.pdm.dto.ComponentDTO;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.service.pdm.PDMService;
import com.cassinisys.plm.service.pdm.PDMVaultService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/pdm/core")
@Api(tags = "PLM.PDM", description = "PDM Related")
public class PDMController extends BaseController {
    @Autowired
    private PDMService pdmService;
    @Autowired
    private PDMVaultService pdmVaultService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(value = "/revisionmasters/{id}", method = RequestMethod.PUT)
    public PDMRevisionMaster updateRevisionMaster(@PathVariable Integer id, @RequestBody PDMRevisionMaster master) {
        return pdmService.updateRevisionMaster(master);
    }

    /* Assembly methods */
    @RequestMapping(value = "/assemblies", method = RequestMethod.POST)
    public PDMAssembly createAssembly(@RequestBody PDMAssembly pdmAssembly) {
        return pdmService.createAssembly(pdmAssembly);
    }

    @RequestMapping(value = "/assemblies/{id}")
    public PDMAssembly getAssembly(@PathVariable Integer id) {
        return pdmService.getAssembly(id);
    }

    @RequestMapping(value = "/assemblies/{id}", method = RequestMethod.PUT)
    public PDMAssembly updateAssembly(@PathVariable Integer id, @RequestBody PDMAssembly pdmAssembly) {
        return pdmService.updateAssembly(pdmAssembly);
    }

    @RequestMapping(value = "/assemblies/{id}", method = RequestMethod.DELETE)
    public void deleteAssembly(@PathVariable Integer id) {
        pdmService.deleteAssembly(id);
    }

    @RequestMapping(value = "/assemblies/{id}/bom", method = RequestMethod.GET)
    public AssemblyDTO getAssemblyBom(@PathVariable Integer id) {
        return pdmService.getAssemblyBom(id);
    }

    @RequestMapping(value = "/assemblies/{id}/bom", method = RequestMethod.POST)
    public void addBomOccurrences(@PathVariable Integer id,
                                  @RequestBody AssemblyDTO assemblyDTO) {
        pdmService.addBomOccurrences(id, assemblyDTO);
    }

    @RequestMapping(value = "/assemblies/{id}/ebom", method = RequestMethod.POST)
    public PLMItemRevision createEBOM(@PathVariable Integer id) {
        return pdmService.createEBOM(id);
    }

    @RequestMapping(value = "/assemblies/{id}/children", method = RequestMethod.GET)
    public List<PDMRevisionedObject> getAssemblyChildren(@PathVariable Integer id) {
        return pdmService.getAssemblyChildren(id);
    }

    @RequestMapping(value = "/assemblies/{id}/children", method = RequestMethod.POST)
    public void addBomOccurrences(@PathVariable Integer id, @RequestBody List<ComponentDTO> components) {
        pdmService.addBomOccurrences(id, components);
    }

    @RequestMapping(value = "/assemblies/{id}/thumbnail", method = RequestMethod.GET)
    public void getAssemblyThumbnail(@PathVariable Integer id, HttpServletResponse response) {
        pdmService.getThumbnail(id, response);
    }

    @RequestMapping(value = "/assemblies/{id}/pdf", method = RequestMethod.GET)
    public void getAssemblyDrawingPDF(@PathVariable Integer id, HttpServletResponse response) {
        pdmService.getAssemblyDrawingPDF(id, response);
    }

    @RequestMapping(value = "/assemblies/{id}/pdf/download", method = RequestMethod.GET)
    public void downloadAssemblyDrawingPDF(@PathVariable Integer id, HttpServletResponse response) {
        pdmService.downloadAssemblyDrawingPDF(id, response);
    }

    @RequestMapping(value = "/parts/{id}/pdf", method = RequestMethod.GET)
    public void getPartDrawingPDF(@PathVariable Integer id, HttpServletResponse response) {
        pdmService.getPartDrawingPDF(id, response);
    }

    @RequestMapping(value = "/parts/{id}/pdf/download", method = RequestMethod.GET)
    public void downloadPartDrawingPDF(@PathVariable Integer id, HttpServletResponse response) {
        pdmService.downloadPartDrawingPDF(id, response);
    }

    @RequestMapping(value = "/assemblies/multiple/[{ids}]")
    public List<PDMAssembly> getMultipleAssemblies(@PathVariable Integer[] ids) {
        return pdmService.getAssemblies(Arrays.asList(ids));
    }

    /* Assembly configurations */
    @RequestMapping(value = "/assemblies/{id}/configurations", method = RequestMethod.POST)
    public PDMAssemblyConfiguration createAssemblyConfiguration(@PathVariable Integer id, @RequestBody PDMAssemblyConfiguration configuration) {
        return pdmService.createAssemblyConfiguration(configuration);
    }

    @RequestMapping(value = "/assemblies/{id}/configurations/{configId}", method = RequestMethod.PUT)
    public PDMAssemblyConfiguration updateAssemblyConfiguration(@PathVariable Integer id,
                                                                @PathVariable Integer configId,
                                                                @RequestBody PDMAssemblyConfiguration configuration) {
        return pdmService.updateAssemblyConfiguration(configuration);
    }

    @RequestMapping(value = "/assemblies/{id}/configurations")
    public List<PDMAssemblyConfiguration> getAssemblyConfigurations(@PathVariable Integer id) {
        return pdmService.getAssemblyConfigurations(id);
    }

    @RequestMapping(value = "/assemblies/{assemblyId}/configurations/{configId}")
    public PDMAssemblyConfiguration getAssemblyConfiguration(@PathVariable Integer assemblyId,
                                                             @PathVariable Integer configId) {
        return pdmService.getAssemblyConfiguration(configId);
    }

    @RequestMapping(value = "/assemblies/{assemblyId}/configurations/{configId}", method = RequestMethod.DELETE)
    public void deleteAssemblyConfiguration(@PathVariable Integer assemblyId,
                                            @PathVariable Integer configId) {
        pdmService.deleteAssemblyConfiguration(configId);
    }

    /* BOM Occurrences */
    @RequestMapping(value = "/assemblies/{assemblyId}/configurations/{configId}/bom", method = RequestMethod.POST)
    public PDMBOMOccurrence createBOMOccurrence(@PathVariable Integer assemblyId,
                                                @PathVariable Integer configId,
                                                @RequestBody PDMBOMOccurrence bomOccurrence) {
        return pdmService.createBOMOccurrence(bomOccurrence);
    }

    @RequestMapping(value = "/assemblies/{assemblyId}/configurations/{configId}/bom")
    public List<PDMBOMOccurrence> getAssemblyBOMOccurrences(@PathVariable Integer assemblyId,
                                                            @PathVariable Integer configId) {
        return pdmService.getBOMOccurrencesByParent(configId);
    }

    @RequestMapping(value = "/assemblies/{assemblyId}/configurations/{configId}/bom/[{ids}]")
    public List<PDMBOMOccurrence> getMultipleBOMOccurrences(@PathVariable Integer assemblyId,
                                                            @PathVariable Integer configId,
                                                            @PathVariable Integer[] ids) {
        return pdmService.getBOMOccurrences(Arrays.asList(ids));
    }

    @RequestMapping(value = "/assemblies/{assemblyId}/configurations/{configId}/bom/{bomId}", method = RequestMethod.DELETE)
    public void deleteBOMOccurrence(@PathVariable Integer assemblyId,
                                    @PathVariable Integer configId,
                                    @PathVariable Integer bomId) {
        pdmService.deleteBOMOccurrence(bomId);
    }

    @RequestMapping(value = "/assemblies", method = RequestMethod.GET)
    public Page<PDMAssembly> getAssembliesByPageable(PageRequest pageRequest, PDMObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pdmService.getAssembliesByPageable(pageable, objectCriteria);
    }

    @RequestMapping(value = "/assemblies/{assemblyId}/assignpartnumbers", method = RequestMethod.GET)
    public void assignAssemblyPartNumber(@PathVariable Integer assemblyId) {
        pdmService.assignPartNumbersToAssembly(assemblyId);
    }

    @RequestMapping(value = "/revisioned/multiple/[{ids}]")
    public List<PLMItem> getItemsFromDesignObjects(@PathVariable Integer[] ids) {
        return pdmService.getItemsFromDesignObjects(Arrays.asList(ids));
    }

    @RequestMapping(value = "/revisioned/{id}", method = RequestMethod.PUT)
    public PDMRevisionedObject updateRevisionedObject(@PathVariable Integer id, @RequestBody PDMRevisionedObject revObject) {
        return pdmService.updateRevisionedObject(id, revObject);
    }

    @RequestMapping(value = "/revisioned/{id}")
    public PDMRevisionedObject getRevisionedObject(@PathVariable Integer id) {
        return pdmService.getRevisionedObject(id);
    }

    @RequestMapping(value = "/revisioned/{id}/file", method = RequestMethod.GET)
    public PDMFileVersion getFileVersionForRevisionedObject(@PathVariable Integer id) {
        return pdmService.getFileVersionForRevisionedObject(id);
    }

    @RequestMapping(value = "/revisioned/{id}/whereused", method = RequestMethod.GET)
    public List<PDMRevisionedObject> getWhereUsed(@PathVariable Integer id) {
        return pdmService.getWhereUsed(id);
    }

    /* Part methods */
    @RequestMapping(value = "/parts", method = RequestMethod.POST)
    public PDMPart createPart(@RequestBody PDMPart pdmPart) {
        return pdmService.createPart(pdmPart);
    }

    @RequestMapping(value = "/parts/{id}")
    public PDMPart getPart(@PathVariable Integer id) {
        return pdmService.getPart(id);
    }

    @RequestMapping(value = "/parts/multiple/[{ids}]")
    public List<PDMPart> getMultipleParts(@PathVariable Integer[] ids) {
        return pdmService.getParts(Arrays.asList(ids));
    }

    @RequestMapping(value = "/parts/{id}/thumbnail", method = RequestMethod.GET)
    public void getPartThumbnail(@PathVariable Integer id, HttpServletResponse response) {
        pdmService.getThumbnail(id, response);
    }

    @RequestMapping(value = "/parts/{id}", method = RequestMethod.PUT)
    public PDMPart updatePart(@PathVariable Integer id, @RequestBody PDMPart pdmPart) {
        return pdmService.updatePart(pdmPart);
    }

    @RequestMapping(value = "/parts/{id}", method = RequestMethod.DELETE)
    public void deletePart(@PathVariable Integer id) {
        pdmService.deletePart(id);
    }


    /* Part configurations */
    @RequestMapping(value = "/parts/{partId}/configurations", method = RequestMethod.POST)
    public PDMPartConfiguration createPartConfiguration(@PathVariable Integer partId,
                                                        @RequestBody PDMPartConfiguration configuration) {
        return pdmService.createPartConfiguration(configuration);
    }

    @RequestMapping(value = "/parts/{partId}/configurations/{configId}", method = RequestMethod.PUT)
    public PDMPartConfiguration updatePartConfiguration(@PathVariable Integer partId,
                                                        @PathVariable Integer configId,
                                                        @RequestBody PDMPartConfiguration configuration) {
        return pdmService.createPartConfiguration(configuration);
    }

    @RequestMapping(value = "/parts/{partId}/configurations")
    public List<PDMPartConfiguration> getPartConfigurations(@PathVariable Integer partId) {
        return pdmService.getPartConfigurations(partId);
    }

    @RequestMapping(value = "/parts/{partId}/configurations/{configId}")
    public PDMPartConfiguration getPartConfiguration(@PathVariable Integer partId,
                                                     @PathVariable Integer configId) {
        return pdmService.getPartConfiguration(configId);
    }

    @RequestMapping(value = "/parts/{partId}/configurations/{configId}", method = RequestMethod.DELETE)
    public void deletePartConfiguration(@PathVariable Integer partId,
                                        @PathVariable Integer configId) {
        pdmService.deletePartConfiguration(configId);
    }

    @RequestMapping(value = "/drawings/{id}")
    public PDMDrawing getDrawing(@PathVariable Integer id) {
        return pdmService.getDrawing(id);
    }

    @RequestMapping(value = "/drawings/templates", method = RequestMethod.POST)
    public PDMDrawingTemplate createDrawingTemplate(@RequestBody PDMDrawingTemplate drawingTemplate) {
        return pdmService.createDrawingTemplate(drawingTemplate);
    }

    @RequestMapping(value = "/drawings/templates/{id}", method = RequestMethod.PUT)
    public PDMDrawingTemplate updateDrawingTemplate(@PathVariable("id") Integer id, @RequestBody PDMDrawingTemplate drawingTemplate) {
        return pdmService.updateDrawingTemplate(id, drawingTemplate);
    }

    @RequestMapping(value = "/drawings/{id}", method = RequestMethod.PUT)
    public PDMDrawing updateDrawing(@PathVariable("id") Integer id, @RequestBody PDMDrawing drawing) {
        return pdmService.updateDrawing(drawing);
    }

    @RequestMapping(value = "/drawings/templates", method = RequestMethod.GET)
    public List<PDMDrawingTemplate> getDrawingTemplates() {
        return pdmService.getDrawingTemplates();
    }

    @RequestMapping(value = "/drawings/templates/{id}", method = RequestMethod.GET)
    public PDMDrawingTemplate getDrawingTemplate(@PathVariable("id") Integer id) {
        return pdmService.getDrawingTemplate(id);
    }

    @RequestMapping(value = "/drawings/templates/{id}", method = RequestMethod.DELETE)
    public void deleteDrawingTemplate(@PathVariable("id") Integer id) {
        pdmService.deleteDrawingTemplate(id);
    }
}
