package com.cassinisys.plm.controller.pqm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.InspectionPlanCriteria;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.QualityObjectDto;
import com.cassinisys.plm.model.pqm.dto.QualityTypeAttributeDto;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.classification.QualityTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/pqm/qualitytypes")
@Api(tags = "PLM.PQM", description = "Quality Related")
public class QualityTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private QualityTypeService qualityTypeService;


    @RequestMapping(method = RequestMethod.POST)
    public PQMQualityType create(@RequestBody PQMQualityType itemType) {
        return qualityTypeService.create(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PQMQualityType update(@PathVariable("id") Integer id,
                                 @RequestBody PQMQualityType itemType) {
        return qualityTypeService.update(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        qualityTypeService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PQMQualityType get(@PathVariable("id") Integer id) {
        return qualityTypeService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PQMQualityType> getAll() {
        return qualityTypeService.getRootTypes();
    }

    @RequestMapping(value = "/{id}/{type}", method = RequestMethod.GET)
    public Object getQualityTypeByIdAndType(@PathVariable("id") Integer id, @PathVariable("type") PLMObjectType objectType) {
        return qualityTypeService.getQualityTypeByIdAndType(id, objectType);
    }

    @RequestMapping(value = "/{id}/{type}/count", method = RequestMethod.GET)
    @Produces({"text/plain"})
    public Integer getObjectsByType(@PathVariable("id") Integer id, @PathVariable("type") PLMObjectType objectType) {
        return qualityTypeService.getObjectsByType(id, objectType);
    }

    @RequestMapping(value = "/{targetId}/attributeseq/{actId}", method = RequestMethod.POST)
    public PQMQualityTypeAttribute changeQualityAttributeSeq(@PathVariable("targetId") Integer targetId,
                                                             @PathVariable("actId") Integer actId) {
        return qualityTypeService.changeQualityAttributeSeq(targetId, actId);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMQualityType> getMultipleQualityTypes(@PathVariable Integer[] ids) {
        return qualityTypeService.getMultipleQualityTypes(Arrays.asList(ids));
    }

    /*----------------------  Inspection Plan Type --------------------------*/

    @RequestMapping(value = "/inspectionplantypes", method = RequestMethod.POST)
    public PQMInspectionPlanType createInspectionPlanType(@RequestBody PQMInspectionPlanType inspectionPlanType) {
        return qualityTypeService.createInspectionPlanType(inspectionPlanType);
    }

    @RequestMapping(value = "/inspectionplantypes/product", method = RequestMethod.POST)
    public PQMProductInspectionPlanType createProductInspectionPlanType(@RequestBody PQMProductInspectionPlanType productInspectionPlanType) {
        return qualityTypeService.createProductInspectionPlanType(productInspectionPlanType);
    }

    @RequestMapping(value = "/inspectionplantypes/material", method = RequestMethod.POST)
    public PQMMaterialInspectionPlanType createMaterialInspectionPlanType(@RequestBody PQMMaterialInspectionPlanType materialInspectionPlanType) {
        return qualityTypeService.createMaterialInspectionPlanType(materialInspectionPlanType);
    }

    @RequestMapping(value = "/inspectionplantypes/{id}", method = RequestMethod.PUT)
    public PQMInspectionPlanType updateInspectionPlanType(@PathVariable("id") Integer id, @RequestBody PQMInspectionPlanType inspectionPlanType) {
        return qualityTypeService.updateInspectionPlanType(id, inspectionPlanType);
    }

    @RequestMapping(value = "/inspectionplantypes/product/{id}", method = RequestMethod.PUT)
    public PQMProductInspectionPlanType updateProductInspectionPlanType(@PathVariable("id") Integer id, @RequestBody PQMProductInspectionPlanType inspectionPlanType) {
        return qualityTypeService.updateProductInspectionPlanType(inspectionPlanType);
    }

    @RequestMapping(value = "/inspectionplantypes/material/{id}", method = RequestMethod.PUT)
    public PQMMaterialInspectionPlanType updateMaterialInspectionPlanType(@PathVariable("id") Integer id, @RequestBody PQMMaterialInspectionPlanType inspectionPlanType) {
        return qualityTypeService.updateMaterialInspectionPlanType(inspectionPlanType);
    }

    @RequestMapping(value = "/inspectionplantypes/{id}", method = RequestMethod.DELETE)
    public void deleteInspectionPlanType(@PathVariable("id") Integer id) {
        qualityTypeService.deleteInspectionPlanType(id);
    }

    @RequestMapping(value = "/inspectionplantypes/product/{id}", method = RequestMethod.DELETE)
    public void deleteProductInspectionPlanType(@PathVariable("id") Integer id) {
        qualityTypeService.deleteProductInspectionPlanType(id);
    }

    @RequestMapping(value = "/inspectionplantypes/material/{id}", method = RequestMethod.DELETE)
    public void deleteMaterialInspectionPlanType(@PathVariable("id") Integer id) {
        qualityTypeService.deleteMaterialInspectionPlanType(id);
    }

    @RequestMapping(value = "/inspectionplantypes/{id}", method = RequestMethod.GET)
    public PQMInspectionPlanType getInspectionPlanType(@PathVariable("id") Integer id) {
        return qualityTypeService.getInspectionPlanType(id);
    }

    @RequestMapping(value = "/inspectionplantypes/product/{id}", method = RequestMethod.GET)
    public PQMProductInspectionPlanType getProductInspectionPlanType(@PathVariable("id") Integer id) {
        return qualityTypeService.getProductInspectionPlanType(id);
    }

    @RequestMapping(value = "/inspectionplantypes/material/{id}", method = RequestMethod.GET)
    public PQMMaterialInspectionPlanType getMaterialInspectionPlanType(@PathVariable("id") Integer id) {
        return qualityTypeService.getMaterialInspectionPlanType(id);
    }

    @RequestMapping(value = "/inspectionplantypes", method = RequestMethod.GET)
    public List<PQMInspectionPlanType> getAllInspectionPlanTypes() {
        return qualityTypeService.getAllInspectionPlanTypes();
    }

    @RequestMapping(value = "/inspectionplantypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMInspectionPlanType> getMultiple(@PathVariable Integer[] ids) {
        return qualityTypeService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/inspectionplantypes/product/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMProductInspectionPlanType> getMultipleProductTypes(@PathVariable Integer[] ids) {
        return qualityTypeService.findMultipleProductTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/inspectionplantypes/material/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMMaterialInspectionPlanType> getMultipleMaterialTypes(@PathVariable Integer[] ids) {
        return qualityTypeService.getMultipleMaterialTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/inspectionplantypes/tree", method = RequestMethod.GET)
    public List<PQMInspectionPlanType> getInspectionPlanTypeTree() {
        return qualityTypeService.getInspectionPlanTypeTree();
    }

    @RequestMapping(value = "/inspectionplantypes/product/tree", method = RequestMethod.GET)
    public List<PQMProductInspectionPlanType> getProductInspectionPlanTypeTree() {
        return qualityTypeService.getProductInspectionPlanTypeTree();
    }

    @RequestMapping(value = "/inspectionplantypes/material/tree", method = RequestMethod.GET)
    public List<PQMMaterialInspectionPlanType> getMaterialInspectionPlanTypeTree() {
        return qualityTypeService.getMaterialInspectionPlanTypeTree();
    }


    /*----------------------  Problem Report Type --------------------------*/

    @RequestMapping(value = "/prtypes", method = RequestMethod.POST)
    public PQMProblemReportType createPrType(@RequestBody PQMProblemReportType problemReportType) {
        return qualityTypeService.createPrType(problemReportType);
    }

    @RequestMapping(value = "/prtypes/{id}", method = RequestMethod.PUT)
    public PQMProblemReportType updatePrType(@PathVariable("id") Integer id, @RequestBody PQMProblemReportType problemReportType) {
        return qualityTypeService.updatePrType(id, problemReportType);
    }

    @RequestMapping(value = "/prtypes/{id}", method = RequestMethod.DELETE)
    public void deletePrType(@PathVariable("id") Integer id) {
        qualityTypeService.deletePrType(id);
    }

    @RequestMapping(value = "/prtypes/{id}", method = RequestMethod.GET)
    public PQMProblemReportType getPrType(@PathVariable("id") Integer id) {
        return qualityTypeService.getPrType(id);
    }

    @RequestMapping(value = "/prtypes", method = RequestMethod.GET)
    public List<PQMProblemReportType> getAllPrTypes() {
        return qualityTypeService.getAllPrTypes();
    }

    @RequestMapping(value = "/prtypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMProblemReportType> getMultiplePrTypes(@PathVariable Integer[] ids) {
        return qualityTypeService.findMultiplePrTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/prtypes/tree", method = RequestMethod.GET)
    public List<PQMProblemReportType> getPrTypeTree() {
        return qualityTypeService.getPrTypeTree();
    }

    /*----------------------  NCR Type --------------------------*/

    @RequestMapping(value = "/ncrtypes", method = RequestMethod.POST)
    public PQMNCRType createNcrType(@RequestBody PQMNCRType ncrType) {
        return qualityTypeService.createNcrType(ncrType);
    }

    @RequestMapping(value = "/ncrtypes/{id}", method = RequestMethod.PUT)
    public PQMNCRType updateNcrType(@PathVariable("id") Integer id, @RequestBody PQMNCRType ncrType) {
        return qualityTypeService.updateNcrType(id, ncrType);
    }

    @RequestMapping(value = "/ncrtypes/{id}", method = RequestMethod.DELETE)
    public void deleteNcrType(@PathVariable("id") Integer id) {
        qualityTypeService.deleteNcrType(id);
    }

    @RequestMapping(value = "/ncrtypes/{id}", method = RequestMethod.GET)
    public PQMNCRType getNcrType(@PathVariable("id") Integer id) {
        return qualityTypeService.getNcrType(id);
    }

    @RequestMapping(value = "/ncrtypes", method = RequestMethod.GET)
    public List<PQMNCRType> getAllNcrTypes() {
        return qualityTypeService.getAllNcrTypes();
    }

    @RequestMapping(value = "/ncrtypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMNCRType> getMultipleNcrTypes(@PathVariable Integer[] ids) {
        return qualityTypeService.findMultipleNcrTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/ncrtypes/tree", method = RequestMethod.GET)
    public List<PQMNCRType> getNcrTypeTree() {
        return qualityTypeService.getNcrTypeTree();
    }

    /*----------------------  QCR Type --------------------------*/

    @RequestMapping(value = "/qcrtypes", method = RequestMethod.POST)
    public PQMQCRType createQcrType(@RequestBody PQMQCRType qcrType) {
        return qualityTypeService.createQcrType(qcrType);
    }

    @RequestMapping(value = "/qcrtypes/{id}", method = RequestMethod.PUT)
    public PQMQCRType updateQcrType(@PathVariable("id") Integer id, @RequestBody PQMQCRType qcrType) {
        return qualityTypeService.updateQcrType(id, qcrType);
    }

    @RequestMapping(value = "/qcrtypes/{id}", method = RequestMethod.DELETE)
    public void deleteQcrType(@PathVariable("id") Integer id) {
        qualityTypeService.deleteQcrType(id);
    }

    @RequestMapping(value = "/qcrtypes/{id}", method = RequestMethod.GET)
    public PQMQCRType getQcrType(@PathVariable("id") Integer id) {
        return qualityTypeService.getQcrType(id);
    }

    @RequestMapping(value = "/qcrtypes", method = RequestMethod.GET)
    public List<PQMQCRType> getAllQcrTypes() {
        return qualityTypeService.getAllQcrTypes();
    }

    @RequestMapping(value = "/qcrtypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMQCRType> getMultipleQcrTypes(@PathVariable Integer[] ids) {
        return qualityTypeService.findMultipleQcrTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/qcrtypes/tree", method = RequestMethod.GET)
    public List<PQMQCRType> getQcrTypeTree() {
        return qualityTypeService.getQcrTypeTree();
    }

    @RequestMapping(value = "/attributes/{attributeId}", method = RequestMethod.GET)
    public List<ObjectAttribute> getUsedQualityTypeAttributes(@PathVariable("attributeId") Integer attributeId) {
        return qualityTypeService.getUsedQualityTypeAttributes(attributeId);
    }

    @RequestMapping(value = "/{objectType}/attributes/{qualityType}", method = RequestMethod.GET)
    public QualityTypeAttributeDto getQualityTypeSelectionAttributes(@PathVariable("objectType") PLMObjectType objectType, @PathVariable("qualityType") String type) {
        return qualityTypeService.getQualityTypeSelectionAttributes(objectType, type);
    }

    @RequestMapping(value = "/{objectType}/object", method = RequestMethod.POST)
    public QualityTypeAttributeDto crateQualityObject(@PathVariable("objectType") PLMObjectType objectType, @RequestBody QualityTypeAttributeDto qualityTypeAttributeDto) {
        return qualityTypeService.createQualityObject(objectType, qualityTypeAttributeDto);
    }

    @RequestMapping(value = "/{id}/{objectType}/workflows", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(@PathVariable("id") Integer id, @PathVariable("objectType") String type) {
        return qualityTypeService.getHierarchyWorkflows(id, type);
    }

    @RequestMapping(value = "quality/analysts", method = RequestMethod.GET)
    public List<Person> getQualityAnalysts(@RequestParam("groupName") String groupName, @RequestParam("permission") String permission) {
        return qualityTypeService.getQualityAnalysts(groupName, permission);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<PQMQualityType> getQualityTypeTree() {
        return qualityTypeService.getClassificationTree();
    }

    @RequestMapping(value = "/all/quality/objects", method = RequestMethod.GET)
    public Page<QualityObjectDto> getQualityObjects(InspectionPlanCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return qualityTypeService.getQualityObjects(criteria, pageable);
    }

// ------------------------------PPAP Type------------------------------------

    @RequestMapping(value = "/ppaptypes", method = RequestMethod.POST)
    public PQMPPAPType createPpapType(@RequestBody PQMPPAPType ppapType) {
        return qualityTypeService.createPpapType(ppapType);
    }

    @RequestMapping(value = "/ppaptypes/{id}", method = RequestMethod.PUT)
    public PQMPPAPType updatePpapType(@PathVariable("id") Integer id, @RequestBody PQMPPAPType ppapType) {
        return qualityTypeService.updatePpapType(id,ppapType);
    }

    @RequestMapping(value = "/ppaptypes/{id}", method = RequestMethod.DELETE)
    public void deletePpapType(@PathVariable("id") Integer id) {
        qualityTypeService.deletePpapType(id);
    }

    @RequestMapping(value = "/ppaptypes/{id}", method = RequestMethod.GET)
    public PQMPPAPType getPpapType(@PathVariable("id") Integer id) {
        return qualityTypeService.getPpapType(id);
    }

    @RequestMapping(value = "/ppaptypes", method = RequestMethod.GET)
    public List<PQMPPAPType> getAllPpapTypes() {
        return qualityTypeService.getAllPpapTypes();
    }

    @RequestMapping(value = "/ppaptypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMPPAPType> getMultiplePpapTypes(@PathVariable Integer[] ids) {
        return qualityTypeService.findMultiplePpapTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/ppaptypes/tree", method = RequestMethod.GET)
    public List<PQMPPAPType> getPpapTypeTree() {
        return qualityTypeService.getPpapTypeTree();
    }

       /*----------------------  Supplier Audit Type --------------------------*/

    @RequestMapping(value = "/supplieraudittypes", method = RequestMethod.POST)
    public PQMSupplierAuditType createSupplierAuditType(@RequestBody PQMSupplierAuditType ncrType) {
        return qualityTypeService.createSupplierAuditType(ncrType);
    }

    @RequestMapping(value = "/supplieraudittypes/{id}", method = RequestMethod.PUT)
    public PQMSupplierAuditType updateSupplierAuditType(@PathVariable("id") Integer id, @RequestBody PQMSupplierAuditType ncrType) {
        return qualityTypeService.updateSupplierAuditType(id, ncrType);
    }

    @RequestMapping(value = "/supplieraudittypes/{id}", method = RequestMethod.DELETE)
    public void deleteSupplierAuditType(@PathVariable("id") Integer id) {
        qualityTypeService.deleteSupplierAuditType(id);
    }

    @RequestMapping(value = "/supplieraudittypes/{id}", method = RequestMethod.GET)
    public PQMSupplierAuditType getSupplierAuditType(@PathVariable("id") Integer id) {
        return qualityTypeService.getSupplierAuditType(id);
    }

    @RequestMapping(value = "/supplieraudittypes", method = RequestMethod.GET)
    public List<PQMSupplierAuditType> getAllSupplierAuditTypes() {
        return qualityTypeService.getAllSupplierAuditTypes();
    }

    @RequestMapping(value = "/supplieraudittypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMSupplierAuditType> getMultipleSupplierAuditTypes(@PathVariable Integer[] ids) {
        return qualityTypeService.findMultipleSupplierAuditTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/supplieraudittypes/tree", method = RequestMethod.GET)
    public List<PQMSupplierAuditType> getSupplierAuditTypeTree() {
        return qualityTypeService.getSupplierAuditTypeTree();
    }

}
