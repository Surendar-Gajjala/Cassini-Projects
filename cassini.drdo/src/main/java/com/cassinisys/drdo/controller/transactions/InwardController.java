package com.cassinisys.drdo.controller.transactions;

import com.cassinisys.drdo.filtering.GatePassCriteria;
import com.cassinisys.drdo.filtering.InwardCriteria;
import com.cassinisys.drdo.filtering.ItemInstanceCriteria;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.ItemInstanceStatusHistory;
import com.cassinisys.drdo.model.dto.*;
import com.cassinisys.drdo.model.transactions.GatePass;
import com.cassinisys.drdo.model.transactions.Inward;
import com.cassinisys.drdo.model.transactions.InwardItem;
import com.cassinisys.drdo.model.transactions.InwardItemInstance;
import com.cassinisys.drdo.service.transactions.GatePassService;
import com.cassinisys.drdo.service.transactions.InwardService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by subramanyam reddy on 16-10-2018.
 */
@RestController
@RequestMapping("drdo/inwards")
public class InwardController extends BaseController {

    @Autowired
    private InwardService inwardService;

    @Autowired
    private GatePassService gatePassService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private FileDownloadService fileDownloadService;

    @RequestMapping(method = RequestMethod.POST)
    public Inward create(@RequestBody Inward inward) {
        return inwardService.create(inward);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Inward update(@PathVariable("id") Integer id,
                         @RequestBody Inward inward) {
        return inwardService.update(inward);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        inwardService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Inward get(@PathVariable("id") Integer id) {
        return inwardService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Inward> getAll() {
        return inwardService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<Inward> getAllInwards(PageRequest pageRequest, InwardCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inwardService.getAllInwards(pageable, criteria);
    }

    @RequestMapping(value = "/items/all", method = RequestMethod.GET)
    public Page<InwardItemInstance> getAllInwardItems(PageRequest pageRequest, InwardCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inwardService.getAllInwardItems(pageable, criteria);
    }

    @RequestMapping(value = "/allInwards", method = RequestMethod.GET)
    public InwardDto getAllInwardsByPermission(PageRequest pageRequest, InwardCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inwardService.getAllInwardsByPermission(pageable, criteria);
    }

    @RequestMapping(value = "{inwardId}/update", method = RequestMethod.PUT)
    public Inward updateInward(@PathVariable("inwardId") Integer inwardId, @RequestBody Inward inward) {
        return inwardService.updateInward(inward);
    }

    @RequestMapping(value = "/bom/{bomId}", method = RequestMethod.GET)
    public List<Inward> getInwardsByBom(@PathVariable("bomId") Integer bomId) {
        return inwardService.getInwardsByBom(bomId);
    }

    @RequestMapping(value = "{inwardId}/report", method = RequestMethod.GET)
    public InwardReportDto getInwardReport(@PathVariable("inwardId") Integer inwardId) {
        return inwardService.getInwardReport(inwardId);
    }

    @RequestMapping(value = "/bom/{bomId}/report", method = RequestMethod.GET)
    public Page<BomInwardReportDto> getBomInwardReport(@PathVariable("bomId") Integer bomId, @RequestParam("searchText") String searchText,
                                                       PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inwardService.getBomInwardReport(bomId, searchText, pageable);
    }

    /*---------------------------------  Gate Pass Methods             ------------------------------------------------*/

    /*@RequestMapping(value = "/gatePass", method = RequestMethod.POST)
    public GatePass createGatePass(@RequestBody GatePass gatePass) {
        return gatePassService.create(gatePass);
    }*/

    @RequestMapping(value = "/gatePass/{id}", method = RequestMethod.PUT)
    public GatePass updateGatePass(@PathVariable("id") Integer id, @RequestBody GatePass gatePass) {
        return gatePassService.update(gatePass);
    }

    @RequestMapping(value = "/gatePass/{id}", method = RequestMethod.DELETE)
    public void deleteGatePass(@PathVariable("id") Integer id) {
        gatePassService.delete(id);
    }

    @RequestMapping(value = "/gatePass", method = RequestMethod.POST)
    public GatePass uploadGatePass(@RequestParam String gatePassNumber, @RequestParam String gatePassDate, MultipartHttpServletRequest request) {
        return gatePassService.uploadGatePass(gatePassNumber, gatePassDate, request.getFileMap());
    }

    @RequestMapping(value = "/gatePass/search", method = RequestMethod.GET)
    public List<GatePass> getAllGatePass(GatePassCriteria gatePassCriteria) {
        return gatePassService.getGatePassByFreeTextSearch(gatePassCriteria);
    }

    @RequestMapping(value = "/gatePass/all", method = RequestMethod.GET)
    public Page<GatePass> getAllGatePasses(PageRequest pageRequest, GatePassCriteria gatePassCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return gatePassService.getAllGatePasses(pageable, gatePassCriteria);
    }

    @RequestMapping(value = "/gatePass/{gatePassId}/{fileId}/download", method = RequestMethod.GET)
    public void downloadItemFile(@PathVariable("gatePassId") Integer gatePassId,
                                 @PathVariable("fileId") Integer fileId,
                                 HttpServletResponse response) {
        GatePass gatePass = gatePassService.get(gatePassId);
        java.io.File file = gatePassService.getGatePassFile(gatePassId, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, gatePass.getGatePass().getName(), file);
        }
    }

    @RequestMapping(value = "/gatePass/{gatePassId}/{fileId}/preview", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("gatePassId") Integer gatePassId,
                                @PathVariable("fileId") Integer fileId,
                                HttpServletResponse response) throws Exception {
        GatePass gatePass = gatePassService.get(gatePassId);
        String fileName = gatePass.getGatePass().getName();
        java.io.File file = gatePassService.getGatePassFile(gatePassId, fileId);
        if (file != null) {
            try {
                String e = URLDecoder.decode(fileName, "UTF-8");
                response.setHeader("Content-disposition", "inline; filename=" + e);
            } catch (UnsupportedEncodingException var6) {
                response.setHeader("Content-disposition", "inline; filename=" + fileName);
            }
            ServletOutputStream e1 = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), e1);
            e1.flush();
        }
    }

    @RequestMapping(value = "/gatePass/{gatePassId}/details", method = RequestMethod.GET)
    public List<GatePassDetailsDto> getGatePassDetails(@PathVariable("gatePassId") Integer gatePassId) {
        return inwardService.getGatePassDetails(gatePassId);
    }

    /*---------------------------------  Inward Status Update Methods  ------------------------------------------------*/

    @RequestMapping(value = "/{inwardId}/update/status", method = RequestMethod.PUT)
    public Inward updateInwardStatus(@PathVariable("inwardId") Integer inwardId, @RequestBody Inward inward) {
        return inwardService.updateInwardStatus(inwardId, inward);
    }

    /*----------------------------------  Inward Items Methods -------------------------------------------------------*/

    @RequestMapping(value = "/{inwardId}/items", method = RequestMethod.POST)
    public List<InwardItem> createInwardItems(@PathVariable("inwardId") Integer inwardId, @RequestBody List<InwardItem> inwardItems) {
        return inwardService.createInwardItems(inwardId, inwardItems);
    }

    @RequestMapping(value = "/{inwardId}/item", method = RequestMethod.POST)
    public InwardItem createInwardItem(@PathVariable("inwardId") Integer inwardId, @RequestBody InwardItem inwardItem) {
        return inwardService.createInwardItem(inwardId, inwardItem);
    }

    @RequestMapping(value = "/{inwardId}/item/{itemId}", method = RequestMethod.DELETE)
    public void deleteInwardItem(@PathVariable("inwardId") Integer inwardId, @PathVariable("itemId") Integer itemId,
                                 @RequestParam("store") Boolean store) {
        inwardService.deleteInwardItem(inwardId, itemId, store);
    }

    @RequestMapping(value = "/{inwardId}/items", method = RequestMethod.GET)
    public List<InwardItem> getInwardItems(@PathVariable("inwardId") Integer inwardId) {
        return inwardService.getInwardItems(inwardId);
    }

    @RequestMapping(value = "/{inward}/acceptItem/{itemId}", method = RequestMethod.PUT)
    public InwardItem acceptInwardItem(@PathVariable("inward") Integer inward, @PathVariable("itemId") Integer itemId, @RequestBody InwardItem inwardItem) {
        return inwardService.acceptInwardItem(inward, itemId, inwardItem);
    }

    @RequestMapping(value = "/{inward}/provisionalAcceptItem/{itemId}", method = RequestMethod.PUT)
    public InwardItem provisionalAcceptItem(@PathVariable("inward") Integer inward, @PathVariable("itemId") Integer itemId,
                                            @RequestParam("provReason") String provReason, @RequestBody InwardItem inwardItem) {
        return inwardService.provisionalAcceptItem(inward, itemId, provReason, inwardItem);
    }

    @RequestMapping(value = "/{inwardId}/item/lot", method = RequestMethod.POST)
    public InwardItem createInwardItemLot(@PathVariable("inwardId") Integer inwardId, @RequestBody InwardItem inwardItem) {
        return inwardService.createInwardItemLot(inwardId, inwardItem);
    }

    @RequestMapping(value = "/{inwardId}/allocate/all", method = RequestMethod.PUT)
    public InwardItem allocateStorageToInwardItem(@PathVariable("inwardId") Integer inwardId, @RequestBody InwardItem inwardItem) {
        return inwardService.allocateStorageToInwardItem(inwardId, inwardItem);
    }

    /*--------------- ------------------ Inward Item Instance Methods  -----------------------------------------------*/

    @RequestMapping(value = "/{inwardId}/itemInstance/{instanceId}", method = RequestMethod.PUT)
    public InwardItemInstance updateInwardItemInstance(@PathVariable("inwardId") Integer inwardId, @PathVariable("instanceId") Integer instanceId,
                                                       @RequestBody InwardItemInstance inwardItemInstance) {
        return inwardService.updateInwardItemInstance(inwardId, inwardItemInstance);
    }

    @RequestMapping(value = "/{inwardId}/itemInstance/{instanceId}/save", method = RequestMethod.PUT)
    public InwardItemInstance saveInwardItemInstance(@PathVariable("inwardId") Integer inwardId, @PathVariable("instanceId") Integer instanceId,
                                                     @RequestBody InwardItemInstance inwardItemInstance) {
        return inwardService.saveInwardItemInstance(inwardId, inwardItemInstance);
    }

    @RequestMapping(value = "/{inwardId}/itemInstance/{instanceId}/update/{updateId}", method = RequestMethod.PUT)
    public InwardItemInstance updateReturnedInwardItemInstance(@PathVariable("inwardId") Integer inwardId, @PathVariable("instanceId") Integer instanceId,
                                                               @PathVariable("updateId") Integer updateId, @RequestBody InwardItemInstance inwardItemInstance) {
        return inwardService.updateReturnedInwardItemInstance(inwardId, inwardItemInstance, updateId);
    }

    @RequestMapping(value = "/{inwardId}/{itemId}/acceptItemInstance", method = RequestMethod.PUT)
    public InwardItem acceptItemInstance(@PathVariable("inwardId") Integer inwardId, @PathVariable("itemId") Integer itemId,
                                         @RequestBody InwardItemInstance inwardItemInstance) {
        return inwardService.acceptItemInstance(inwardId, itemId, inwardItemInstance);
    }

    @RequestMapping(value = "/{inwardId}/{itemId}/acceptItemInstance/later", method = RequestMethod.PUT)
    public InwardItem acceptItemInstanceLater(@PathVariable("inwardId") Integer inwardId, @PathVariable("itemId") Integer itemId,
                                              @RequestBody InwardItemInstance inwardItemInstance) {
        return inwardService.acceptItemInstanceLater(inwardId, itemId, inwardItemInstance);
    }

    @RequestMapping(value = "/{inwardId}/{itemId}/provAcceptItemInstance", method = RequestMethod.PUT)
    public InwardItem provisionalAcceptItemInstance(@PathVariable("inwardId") Integer inwardId, @PathVariable("itemId") Integer itemId,
                                                    @RequestBody InwardItemInstance inwardItemInstance) {
        return inwardService.provisionalAcceptItemInstance(inwardId, itemId, inwardItemInstance);
    }

    @RequestMapping(value = "/{inwardId}/returnItemInstance", method = RequestMethod.PUT)
    public InwardItem returnItemInstance(@PathVariable("inwardId") Integer inwardId, @RequestBody InwardItemInstance inwardItemInstance) {
        return inwardService.returnItemInstance(inwardId, inwardItemInstance);
    }

    @RequestMapping(value = "/{inwardId}/allocate/returnStorage", method = RequestMethod.PUT)
    public InwardItem addStorageToReturnItemInstance(@PathVariable("inwardId") Integer inwardId, @RequestBody InwardItemInstance inwardItemInstance) {
        return inwardService.addStorageToReturnItemInstance(inwardId, inwardItemInstance);
    }

    @RequestMapping(value = "/returnItemInstance", method = RequestMethod.GET)
    public ItemInstance getReturnedInstanceByUpnNumber(@RequestParam("upnNumber") String upnNumber, @RequestParam("serialNumber") String serialNumber) {
        return inwardService.getReturnedInstanceByUpnNumber(upnNumber, serialNumber);
    }

    @RequestMapping(value = "/returnItemInstance/{itemId}", method = RequestMethod.GET)
    public ItemInstance getReturnedInstanceBySerialNumberAndManufacturerAndItem(@PathVariable("itemId") Integer itemId,
                                                                                @RequestParam("serialNumber") String serialNumber, @RequestParam("mfr") Integer mfr) {
        return inwardService.getReturnedInstanceBySerialNumberAndManufacturerAndItem(itemId, serialNumber, mfr);
    }

    @RequestMapping(value = "/{inwardId}/reviewItemInstance", method = RequestMethod.PUT)
    public InwardItem reviewItemInstance(@PathVariable("inwardId") Integer inwardId, @RequestBody InwardItemInstance inwardItemInstance) {
        return inwardService.reviewItemInstance(inwardId, inwardItemInstance);
    }

    @RequestMapping(value = "/{inwardId}/allocate", method = RequestMethod.PUT)
    public InwardItem allocateStorage(@PathVariable("inwardId") Integer inwardId, @RequestBody InwardItemInstance inwardItemInstance) {
        return inwardService.allocateStorage(inwardId, inwardItemInstance);
    }

    @RequestMapping(value = "/{inwardId}/item/{inwardInstanceId}/verify/{storageName}/{upnNumber}", method = RequestMethod.GET)
    public InwardItem verifyStoragePart(@PathVariable("inwardId") Integer inwardId, @PathVariable("inwardInstanceId") Integer inwardInstanceId,
                                        @PathVariable("storageName") String storageName, @PathVariable("upnNumber") String upnNumber) {
        return inwardService.verifyStoragePart(inwardId, inwardInstanceId, storageName, upnNumber);
    }

    @RequestMapping(value = "/returnItems/all", method = RequestMethod.GET)
    public Page<ItemInstance> getAllReturnItems(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inwardService.getAllReturnItems(pageable);
    }

    @RequestMapping(value = "/failureItems/all", method = RequestMethod.GET)
    public Page<ItemInstance> getAllFailureItems(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inwardService.getAllFailureItems(pageable);
    }

    @RequestMapping(value = "/expiredItems/all", method = RequestMethod.GET)
    public Page<ItemInstance> getAllExpiredItems(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inwardService.getAllExpiredItems(pageable);
    }

    @RequestMapping(value = "/toExpire/all", method = RequestMethod.GET)
    public Page<ItemInstance> getToExpireItems(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inwardService.getToExpireItems(pageable);
    }

    @RequestMapping(value = "/itemInstances/search", method = RequestMethod.GET)
    private Page<ItemInstance> searchItemInstances(PageRequest pageRequest, ItemInstanceCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inwardService.searchItemInstances(pageable, criteria);
    }

    /*-------------------------------------  Inward Attribute Methods   ---------------------------------------------*/

    @RequestMapping(value = "/[{ids}]/lot/attributes/multiple", method = RequestMethod.POST)
    public List<ObjectAttribute> createMultipleLotAttributes(@PathVariable Integer[] ids, @RequestBody List<ObjectAttribute> attributes) {
        return inwardService.createMultipleLotAttributes(Arrays.asList(ids), attributes);
    }

    @RequestMapping(value = "/multipleLot/updateImage/[{objectIds}]/{attributeId}", method = RequestMethod.POST)
    public List<ObjectAttribute> saveMultipleLotImage(@PathVariable("objectIds") Integer[] objectIds,
                                                      @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return inwardService.saveMultipleLotImage(Arrays.asList(objectIds), attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/multipleLot/attachment/[{objectIds}]/{attributeId}", method = RequestMethod.POST)
    public List<AttributeAttachment> uploadMultiLotAttachment(@PathVariable("objectIds") Integer[] objectIds, @PathVariable("attributeId") Integer attributeId,
                                                              MultipartHttpServletRequest request) {
        return inwardService.uploadMultiLotAttachment(Arrays.asList(objectIds), attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/instances/objectAttributes", method = RequestMethod.POST)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByItemAndAttributeId(@RequestBody List<Integer[]> ids) {

        Integer[] objectIds = ids.get(0);
        Integer[] attIds = ids.get(1);
        return inwardService.getObjectAttributesByItemIdsAndAttributeIds(objectIds, attIds);
    }

    @RequestMapping(value = "/{instanceId}/saveAttribute", method = RequestMethod.POST)
    public ObjectAttribute saveInstanceAttribute(@PathVariable("instanceId") Integer instanceId, @RequestBody ObjectAttribute objectAttribute) {
        return inwardService.saveInstanceAttribute(instanceId, objectAttribute);
    }

    @RequestMapping(value = "/{instanceId}/saveAttributes/multiple", method = RequestMethod.PUT)
    public List<ObjectAttribute> saveInstanceAttributes(@PathVariable("instanceId") Integer instanceId, @RequestBody List<ObjectAttribute> objectAttributes) {
        return inwardService.saveInstanceAttributes(instanceId, objectAttributes);
    }

    @RequestMapping(value = "/{instanceId}/saveImage/{attributeId}", method = RequestMethod.POST)
    public ObjectAttribute saveInstanceImage(@PathVariable("instanceId") Integer instanceId, @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return inwardService.saveInstanceImage(instanceId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/{instanceId}/saveAttachments/{attributeId}", method = RequestMethod.POST)
    public ObjectAttribute uploadInstanceAttachment(@PathVariable("instanceId") Integer instanceId, @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return inwardService.uploadInstanceAttachment(instanceId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/{instanceId}/details/apply", method = RequestMethod.POST)
    public List<ObjectAttribute> applyDetails(@PathVariable("instanceId") Integer instanceId, @RequestBody List<InwardItemInstance> inwardItemInstances) {
        return inwardService.applyDetails(instanceId, inwardItemInstances);
    }

    @RequestMapping(value = "/{instanceId}/details/update/apply", method = RequestMethod.POST)
    public ObjectAttribute updateInstanceAttributeDetails(@PathVariable("instanceId") Integer instanceId, @RequestBody ObjectAttribute objectAttribute) {
        return inwardService.updateInstanceAttributeDetails(instanceId, objectAttribute);
    }

    @RequestMapping(value = "/{instanceId}/item/status/history", method = RequestMethod.GET)
    public List<ItemInstanceStatusHistory> getItemStatusHistory(@PathVariable("instanceId") Integer instanceId) {
        return inwardService.getItemStatusHistory(instanceId);
    }

    @RequestMapping(value = "/{instanceId}/item/upnDetails", method = RequestMethod.GET)
    public UpnDetailsDto getUpnDetails(@PathVariable("instanceId") Integer instanceId) {
        return inwardService.getUpnDetails(instanceId);
    }

    @RequestMapping(value = "/{instanceId}/item/lot/upnDetails", method = RequestMethod.GET)
    public UpnDetailsDto getLotUpnDetails(@PathVariable("instanceId") Integer instanceId) {
        return inwardService.getLotUpnDetails(instanceId);
    }

    @RequestMapping(value = "/{instanceId}/item/lot/issued/upnDetails", method = RequestMethod.GET)
    public UpnDetailsDto getIssuedLotUpnDetails(@PathVariable("instanceId") Integer instanceId) {
        return inwardService.getIssuedLotUpnDetails(instanceId);
    }

    @RequestMapping(value = "/{attributeDef}/delete/attachment/{attachmentId}", method = RequestMethod.PUT)
    public ObjectAttribute deleteInstanceAttributeAttachment(@PathVariable("attributeDef") Integer attributeDef, @PathVariable("attachmentId") Integer attachmentId,
                                                             @RequestBody Integer[] instanceIds) {
        return inwardService.deleteInstanceAttributeAttachment(attributeDef, attachmentId, instanceIds);
    }

    @RequestMapping(value = "/instance/barcode/{upnNumber}", method = RequestMethod.GET)
    public void generateInstanceBarcode(@PathVariable("upnNumber") String upnNumber,
                                        HttpServletResponse response) {
        inwardService.generateInstanceBarcode(upnNumber, response);
    }

    @RequestMapping(value = "/check/{supplier}", method = RequestMethod.GET)
    public Boolean checkWithSupplier(@PathVariable("supplier") Integer supplier) {
        return inwardService.checkInwardsWithSupplier(supplier);
    }

    @RequestMapping(value = "/{itemInstance}/next/lotNumber", method = RequestMethod.GET)
    public ItemInstance nextLotNumber(@PathVariable("itemInstance") Integer itemInstance) {
        return inwardService.nextLotNumber(itemInstance);
    }

    @RequestMapping(value = "/{itemInstance}/delete/lotNumber", method = RequestMethod.GET)
    public ItemInstance deleteGeneratedLotNumber(@PathVariable("itemInstance") Integer itemInstance) {
        return inwardService.deleteGeneratedLotNumber(itemInstance);
    }


    @RequestMapping(value = "/{itemInstanceId}/generate/rootCardNumber", method = RequestMethod.GET)
    public ItemInstance generateRootCardNumber(@PathVariable("itemInstanceId") Integer itemInstanceId) {
        return inwardService.generateRootCardNumber(itemInstanceId);
    }
}
