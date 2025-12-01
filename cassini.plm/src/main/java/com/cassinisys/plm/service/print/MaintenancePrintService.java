package com.cassinisys.plm.service.print;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.plm.config.MessageResolverMethod;
import com.cassinisys.plm.model.dto.Print;
import com.cassinisys.plm.model.dto.print.PrintAttributesDTO;
import com.cassinisys.plm.model.dto.print.PrintDTO;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.mro.dto.WorkOrderDto;
import com.cassinisys.plm.repo.mro.*;
import com.cassinisys.plm.service.mro.AssetService;
import com.cassinisys.plm.service.mro.MaintenancePlanService;
import com.cassinisys.plm.service.mro.SparePartsService;
import com.cassinisys.plm.service.mro.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/*
* Created by Suresh Cassini on 24-12-2020.
* */
@Service
public class MaintenancePrintService {
    private PrintDTO printDTO;
    @Autowired
    private PrintService printService;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private PrintAttributesDTO printAttributesDTO;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private MROAssetRepository assetRepository;
    @Autowired
    private AssetService assetService;
    @Autowired
    private MROObjectTypeAttributeRepository mroObjectTypeAttributeRepository;
    @Autowired
    private MROObjectAttributeRepository mroObjectAttributeRepository;
    @Autowired
    private MROMeterRepository mroMeterRepository;
    @Autowired
    private MROSparePartRepository sparePartRepository;
    @Autowired
    private SparePartsService sparePartsService;
    @Autowired
    private MROMaintenancePlanRepository maintenancePlanRepository;
    @Autowired
    private MaintenancePlanService maintenancePlanService;
    @Autowired
    private MROWorkRequestRepository mroWorkRequestRepository;
    @Autowired
    private MROWorkOrderRepository mroWorkOrderRepository;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private MessageSource messageSource;

    private String getResourceImage(byte[] thumbnail) {
        String baseString = "";
        byte[] imgBytesAsBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(thumbnail);
        String imgDataAsBase64 = new String(imgBytesAsBase64);
        baseString = "data:image/png;base64," + imgDataAsBase64;
        return baseString;
    }

    public String getAssetTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MROAsset asset = this.getAssetDetails(print);
                this.printDTO.setAsset(asset);
                PrintDTO dtoForAttributes = this.getMROObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
            if (option.equals("Spare Parts")) {
                this.printDTO.setSpareParts(this.getAssetSpareParts(print));
            }

            if (option.equals("Maintenance")) {
                this.printDTO.setMaintenancePlans(this.getAssetWorkOrderMaintenancePlans(print));

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("maintenanceData", this.printDTO);
        String templatePath = "maintenanceTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MROAsset getAssetDetails(Print print) throws ParseException {
        MROAsset asset = assetService.get(print.getObjectId());

        return asset;
    }

    private List<MROAssetSparePart> getAssetSpareParts(Print print) throws ParseException {
        List<MROAssetSparePart> assetSpareParts = assetService.getAssetSpareParts(print.getObjectId());
        return assetSpareParts;
    }


    private List<WorkOrderDto> getAssetWorkOrderMaintenancePlans(Print print) throws ParseException {
        List<WorkOrderDto> maintenanceplans = assetService.getAllWorkOrdersByAsset(print.getObjectId());
        return maintenanceplans;
    }


    public String getMeterTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MROMeter meter = this.getMeterDetails(print);
                this.printDTO.setMeter(meter);
                PrintDTO dtoForAttributes = this.getMROObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("maintenanceData", this.printDTO);
        String templatePath = "maintenanceTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    private MROMeter getMeterDetails(Print print) throws ParseException {
        MROMeter meter = mroMeterRepository.findOne(print.getObjectId());
        if (meter != null) {
            Measurement measurement = measurementRepository.findOne(meter.getQom());
            MeasurementUnit unit = measurementUnitRepository.findOne(meter.getUom());
            meter.setMeasurementName(measurement.getName());
            meter.setUnitName(unit.getName());
            meter.setMeterTypeName(meter.getMeterType().toString());
            meter.setMeterReadingTypeName(meter.getMeterReadingType().toString());
            Person person = personRepository.findOne(meter.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(meter.getModifiedBy());
            meter.setCreatePerson(person.getFirstName());
            meter.setTypeName(meter.getType().getName());
            meter.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return meter;
    }


    public String getSparePartTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MROSparePart sparePart = this.getSparePartsDetails(print);
                this.printDTO.setSparePart(sparePart);
                PrintDTO dtoForAttributes = this.getMROObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("maintenanceData", this.printDTO);
        String templatePath = "maintenanceTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MROSparePart getSparePartsDetails(Print print) throws ParseException {
        MROSparePart sparePart = sparePartRepository.findOne(print.getObjectId());
        if (sparePart != null) {
            Person person = personRepository.findOne(sparePart.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(sparePart.getModifiedBy());
            sparePart.setCreatePerson(person.getFirstName());
            sparePart.setTypeName(sparePart.getType().getName());
            sparePart.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return sparePart;
    }


    public String getMaintenanceTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MROMaintenancePlan maintenancePlan = this.getMaintenancePlanDetails(print);
                this.printDTO.setMaintenancePlan(maintenancePlan);

            }
            if (option.equals("Operations")) {
                this.printDTO.setMaintenanceOperations(this.getMaintenanceOperations(print));
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("maintenanceData", this.printDTO);
        String templatePath = "maintenanceTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    private MROMaintenancePlan getMaintenancePlanDetails(Print print) throws ParseException {
        MROMaintenancePlan maintenancePlan = maintenancePlanRepository.findOne(print.getObjectId());
        if (maintenancePlan != null) {
            Person person = personRepository.findOne(maintenancePlan.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(maintenancePlan.getModifiedBy());
            maintenancePlan.setAssetName(assetRepository.findOne(maintenancePlan.getAsset()).getName());
            maintenancePlan.setCreatePerson(person.getFirstName());
            maintenancePlan.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return maintenancePlan;
    }

    private List<MROMaintenanceOperation> getMaintenanceOperations(Print print) {
        List<MROMaintenanceOperation> operations = maintenancePlanService.getMaintenancePlanOperations(print.getObjectId());
        return operations;
    }

    public String getWorkRequestTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MROWorkRequest workRequest = this.getWorkRequestDetails(print);
                this.printDTO.setWorkRequest(workRequest);
                PrintDTO dtoForAttributes = this.getMROObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }

        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("maintenanceData", this.printDTO);
        String templatePath = "maintenanceTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MROWorkRequest getWorkRequestDetails(Print print) throws ParseException {
        MROWorkRequest workRequest = mroWorkRequestRepository.findOne(print.getObjectId());
        if (workRequest != null) {
            Person person = personRepository.findOne(workRequest.getCreatedBy());
            Person requestor = personRepository.findOne(workRequest.getRequestor());
            Person modifiedPerson = personRepository.findOne(workRequest.getModifiedBy());
            workRequest.setAssetName(assetRepository.findOne(workRequest.getAsset()).getName());
            workRequest.setPriorityName(workRequest.getPriority().toString());
            workRequest.setStatusName(workRequest.getStatus().toString());
            workRequest.setCreatePerson(person.getFirstName());
            workRequest.setTypeName(workRequest.getType().getName());
            workRequest.setRequestorPerson(requestor.getFirstName());
            workRequest.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return workRequest;
    }


    public String getWorkOrderTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MROWorkOrder workOrder = this.getWorkOrderDetails(print);
                this.printDTO.setWorkOrder(workOrder);
                PrintDTO dtoForAttributes = this.getMROObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
            if (option.equals("Operations")) {
                this.printDTO.setWorkOrderOperations(workOrderService.getWorkOrderOperations(print.getObjectId()));
            }

            if (option.equals("Resources")) {
                this.printDTO.setWorkOrderResources(workOrderService.getAllWorkOrderResources(print.getObjectId()));
            }

            if (option.equals("Spare Parts")) {
                this.printDTO.setWorkOrderParts(workOrderService.getWorkOrderSpareParts(print.getObjectId()));
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("maintenanceData", this.printDTO);
        String templatePath = "maintenanceTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    private MROWorkOrder getWorkOrderDetails(Print print) throws ParseException {
        MROWorkOrder workOrder = mroWorkOrderRepository.findOne(print.getObjectId());
        if (workOrder != null) {
            if (workOrder.getRequest() != null) {
                MROWorkRequest request = mroWorkRequestRepository.findOne(workOrder.getRequest());
                workOrder.setRequestNumber(request.getNumber());
            }
            if (workOrder.getPlan() != null) {
                MROMaintenancePlan plan = maintenancePlanRepository.findOne(workOrder.getPlan());
                workOrder.setMaintenancePlan(plan.getNumber());
            }
            Person person = personRepository.findOne(workOrder.getCreatedBy());
            Person assighnedPerson = personRepository.findOne(workOrder.getAssignedTo());
            Person modifiedPerson = personRepository.findOne(workOrder.getModifiedBy());
            workOrder.setAssetName(assetRepository.findOne(workOrder.getAsset()).getName());
            workOrder.setTypeName(workOrder.getType().getName());
            workOrder.setPriorityName(workOrder.getPriority().toString());
            workOrder.setStatusName(workOrder.getStatus().toString());
            workOrder.setCreatePerson(person.getFirstName());
            workOrder.setAssignedPerson(assighnedPerson.getFirstName());
            workOrder.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return workOrder;
    }


    private PrintDTO getMROObjectTypeAttributes(Integer objectId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> mesAttributes = new LinkedList<>();
        List<PrintAttributesDTO> masters = new ArrayList<>();
        List<MROObjectAttribute> masterAttributes = mroObjectAttributeRepository.findByObjectId(objectId);
        for (int i = 0; i < masterAttributes.size(); i++) {
            MROObjectAttribute masterAttr = masterAttributes.get(i);
            MROObjectTypeAttribute itemTypeAttribute = mroObjectTypeAttributeRepository.findOne(masterAttr.getId().getAttributeDef());
            PrintAttributesDTO master = printAttributesDTO.getMroObjectAttributes(itemTypeAttribute, masterAttr);
            if (itemTypeAttribute.getAttributeGroup() != null && itemTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(itemTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                mesAttributes.add(master);
            }
        }
        Map<String, List<PrintAttributesDTO>> attributesMaterForGroup = this.printService.getAttributeByGroup(masters);
        dto.setItemMasterAttributes(mesAttributes);
        dto.setGroupMasterAttributes(attributesMaterForGroup);
        return dto;
    }

}