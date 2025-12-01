package com.cassinisys.plm.controller.pqm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.QCRCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.PRItemsDto;
import com.cassinisys.plm.model.pqm.dto.QCRsDto;
import com.cassinisys.plm.service.pqm.QCRService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by subramanyam on 04-06-2020.
 */
@RestController
@RequestMapping("/pqm/qcrs")
@Api(tags = "PLM.PQM", description = "Quality Related")
public class QCRController extends BaseController {

    @Autowired
    private QCRService qcrService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PQMQCR create(@RequestBody PQMQCR qcr) {
        return qcrService.create(qcr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PQMQCR update(@PathVariable("id") Integer id,
                         @RequestBody PQMQCR qcr) {
        return qcrService.update(qcr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        qcrService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PQMQCR get(@PathVariable("id") Integer id) {
        return qcrService.get(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public QCRsDto getQCRDetails(@PathVariable("id") Integer id) {
        return qcrService.getQCRDetails(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PQMQCR> getAll() {
        return qcrService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMQCR> getMultiple(@PathVariable Integer[] ids) {
        return qcrService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<QCRsDto> getAllQCRs(PageRequest pageRequest, QCRCriteria qcrCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return qcrService.getAllQcrs(pageable, qcrCriteria);
    }

    @RequestMapping(value = "/released/{qcrFor}", method = RequestMethod.GET)
    public List<PQMQCR> getReleasedByQcrFor(@PathVariable("qcrFor") QCRFor qcrFor) {
        return qcrService.getReleasedByQcrFor(qcrFor);
    }

    @RequestMapping(value = "/{id}/files", method = RequestMethod.GET)
    public List<PQMQCRFile> getQcrFiles(@PathVariable("id") Integer id) {
        return qcrService.getQcrFiles(id);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PQMQCRAttribute createQcrAttribute(@PathVariable("id") Integer id,
                                              @RequestBody PQMQCRAttribute attribute) {
        return qcrService.createQcrAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PQMQCRAttribute updateQcrAttribute(@PathVariable("id") Integer id,
                                              @RequestBody PQMQCRAttribute attribute) {
        return qcrService.updateQcrAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/sources/pr", method = RequestMethod.POST)
    public List<PQMQCRAggregatePR> createPrProblemSources(@PathVariable("id") Integer id, @RequestBody List<PQMQCRAggregatePR> aggregatePRs) {
        return qcrService.createPrProblemSources(id, aggregatePRs);
    }

    @RequestMapping(value = "/{id}/sources/ncr", method = RequestMethod.POST)
    public List<PQMQCRAggregateNCR> createNcrProblemSources(@PathVariable("id") Integer id, @RequestBody List<PQMQCRAggregateNCR> aggregateNCRs) {
        return qcrService.createNcrProblemSources(id, aggregateNCRs);
    }

    @RequestMapping(value = "/{id}/sources/pr", method = RequestMethod.GET)
    public List<PQMQCRAggregatePR> getPrProblemSources(@PathVariable("id") Integer id) {
        return qcrService.getPrProblemSources(id);
    }

    @RequestMapping(value = "/{id}/sources/ncr", method = RequestMethod.GET)
    public List<PQMQCRAggregateNCR> getNcrProblemSources(@PathVariable("id") Integer id) {
        return qcrService.getNcrProblemSources(id);
    }

    @RequestMapping(value = "/{id}/sources/pr/{sourceId}", method = RequestMethod.DELETE)
    public void deletePrProblemSource(@PathVariable("id") Integer id, @PathVariable("sourceId") Integer sourceId) {
        qcrService.deletePrProblemSource(id, sourceId);
    }

    @RequestMapping(value = "/{id}/sources/ncr/{sourceId}", method = RequestMethod.DELETE)
    public void deleteNcrProblemSource(@PathVariable("id") Integer id, @PathVariable("sourceId") Integer sourceId) {
        qcrService.deleteNcrProblemSource(id, sourceId);
    }

    /*------------------------------------  Problem Items -----------------------------------------------------*/

    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public List<PRItemsDto> getQcrProblemItems(@PathVariable("id") Integer id) {
        return qcrService.getQcrProblemItems(id);
    }

    @RequestMapping(value = "/{id}/items/multiple", method = RequestMethod.POST)
    public List<PQMQCRProblemItem> createQcrProblemItems(@PathVariable("id") Integer id, @RequestBody List<PQMQCRProblemItem> problemItems) {
        return qcrService.createQcrProblemItems(id, problemItems);
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.POST)
    public PQMQCRProblemItem createQcrProblemItem(@PathVariable("id") Integer id, @RequestBody PQMQCRProblemItem problemItem) {
        return qcrService.createQcrProblemItem(id, problemItem);
    }

    @RequestMapping(value = "/{id}/items/{itemId}", method = RequestMethod.PUT)
    public PQMQCRProblemItem updateQcrProblemItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId,
                                                  @RequestBody PQMQCRProblemItem problemItem) {
        return qcrService.updateQcrProblemItem(id, problemItem);
    }

    @RequestMapping(value = "/{id}/items/{itemId}", method = RequestMethod.DELETE)
    public void deleteQcrProblemItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        qcrService.deleteQcrProblemItem(id, itemId);
    }

    @RequestMapping(value = "/{id}/materials/{materialId}", method = RequestMethod.DELETE)
    public void deleteQcrProblemMaterial(@PathVariable("id") Integer id, @PathVariable("materialId") Integer materialId) {
        qcrService.deleteQcrProblemMaterial(id, materialId);
    }

    @RequestMapping(value = "/{id}/materials", method = RequestMethod.GET)
    public List<PQMQCRProblemMaterial> getQcrProblemMaterials(@PathVariable("id") Integer id) {
        return qcrService.getQcrProblemMaterials(id);
    }

    @RequestMapping(value = "/{id}/materials", method = RequestMethod.POST)
    public PQMQCRProblemMaterial createQcrProblemMaterial(@PathVariable("id") Integer id, @RequestBody PQMQCRProblemMaterial problemMaterial) {
        return qcrService.createQcrProblemMaterial(id, problemMaterial);
    }

    @RequestMapping(value = "/{id}/materials/{materialId}", method = RequestMethod.PUT)
    public PQMQCRProblemMaterial updateQcrProblemMaterial(@PathVariable("id") Integer id, @PathVariable("materialId") Integer materialId,
                                                          @RequestBody PQMQCRProblemMaterial problemMaterial) {
        return qcrService.updateQcrProblemMaterial(id, problemMaterial);
    }

    @RequestMapping(value = "/{id}/materials/multiple", method = RequestMethod.POST)
    public List<PQMQCRProblemMaterial> createQcrProblemMaterials(@PathVariable("id") Integer id, @RequestBody List<PQMQCRProblemMaterial> problemMaterials) {
        return qcrService.createQcrProblemMaterials(id, problemMaterials);
    }

    /*-----------------------------------  Related Items -------------------------------*/

    @RequestMapping(value = "/{id}/relatedItems", method = RequestMethod.POST)
    public List<PQMQCRRelatedItem> createQcrRelatedItem(@PathVariable("id") Integer id, @RequestBody List<PQMQCRRelatedItem> relatedItems) {
        return qcrService.createQcrRelatedItems(id, relatedItems);
    }

    @RequestMapping(value = "/{id}/relatedMaterials", method = RequestMethod.POST)
    public List<PQMQCRRelatedMaterial> createQcrRelatedMaterials(@PathVariable("id") Integer id, @RequestBody List<PQMQCRRelatedMaterial> relatedMaterials) {
        return qcrService.createQcrRelatedMaterials(id, relatedMaterials);
    }

    @RequestMapping(value = "/{id}/relatedMaterials", method = RequestMethod.GET)
    public List<PQMQCRRelatedMaterial> getQcrRelatedMaterials(@PathVariable("id") Integer id) {
        return qcrService.getQcrRelatedMaterials(id);
    }

    @RequestMapping(value = "/{id}/relatedItems", method = RequestMethod.GET)
    public List<PRItemsDto> getQcrRelatedItems(@PathVariable("id") Integer id) {
        return qcrService.getQcrRelatedItems(id);
    }

    @RequestMapping(value = "/{id}/relatedMaterials/{material}", method = RequestMethod.DELETE)
    public void deleteQcrRelatedMaterial(@PathVariable("id") Integer id, @PathVariable("material") Integer material) {
        qcrService.deleteQcrRelatedMaterial(id, material);
    }

    @RequestMapping(value = "/{id}/relatedItems/{itemId}", method = RequestMethod.DELETE)
    public void deleteQcrRelatedItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        qcrService.deleteQcrRelatedItem(id, itemId);
    }

    @RequestMapping(value = "{id}/details/count", method = RequestMethod.GET)
    public ItemDetailsDto getDetailsCount(@PathVariable("id") Integer id) {
        return qcrService.getDetailsCount(id);
    }

    @RequestMapping(value = "{id}/capa", method = RequestMethod.POST)
    public PQMQCRCAPA createQCRCaPa(@PathVariable("id") Integer id, @RequestBody PQMQCRCAPA pqmqcrcapa) {
        return qcrService.createQCRCaPa(id, pqmqcrcapa);
    }

    @RequestMapping(value = "{id}/capa/{capaId}", method = RequestMethod.PUT)
    public PQMQCRCAPA updateQCRCaPa(@PathVariable("id") Integer id, @PathVariable("capaId") Integer capaId,
                                    @RequestBody PQMQCRCAPA pqmqcrcapa) {
        return qcrService.updateQCRCaPa(id, pqmqcrcapa);
    }

    @RequestMapping(value = "{id}/capa/{capaId}/audit", method = RequestMethod.PUT)
    public PQMQCRCAPA updateQCRCaPaAudit(@PathVariable("id") Integer id, @PathVariable("capaId") Integer capaId,
                                         @RequestBody PQMQCRCAPA pqmqcrcapa) {
        return qcrService.updateQCRCaPaAudit(id, pqmqcrcapa);
    }

    @RequestMapping(value = "{id}/capa/{capaId}", method = RequestMethod.DELETE)
    public void deleteQCRCaPa(@PathVariable("id") Integer id, @PathVariable("capaId") Integer capaId) {
        qcrService.deleteQCRCaPa(id, capaId);
    }

    @RequestMapping(value = "{id}/capa/{capaId}", method = RequestMethod.GET)
    public PQMQCRCAPA getQCRCaPa(@PathVariable("id") Integer id, @PathVariable("capaId") Integer capaId) {
        return qcrService.getQCRCaPa(id, capaId);
    }

    @RequestMapping(value = "{id}/capa", method = RequestMethod.GET)
    public List<PQMQCRCAPA> getAllQCRCaPa(@PathVariable("id") Integer id) {
        return qcrService.getAllQCRCaPa(id);
    }
}
