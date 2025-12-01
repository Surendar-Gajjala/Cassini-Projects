package com.cassinisys.plm.service.print;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.plm.config.MessageResolverMethod;
import com.cassinisys.plm.model.dto.Print;
import com.cassinisys.plm.model.dto.print.PrintAttributesDTO;
import com.cassinisys.plm.model.dto.print.PrintDTO;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.service.cm.MCOService;
import com.cassinisys.plm.service.cm.PLMVarianceService;
import com.cassinisys.plm.service.mfr.ManufacturerPartService;
import com.cassinisys.plm.service.mfr.ManufacturerService;
import com.cassinisys.plm.service.mfr.SupplierService;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.pqm.NCRService;
import com.cassinisys.plm.service.pqm.QCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

@Service
public class SourcingPrintService {
    private PrintDTO printDTO;
    @Autowired
    private PrintService printService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private PrintAttributesDTO printAttributesDTO;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private ManufacturerPartService manufacturerPartService;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;
    @Autowired
    private PLMVarianceService varianceService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private MCOService mcoService;
    @Autowired
    private QCRService qcrService;
    @Autowired
    private NCRService ncrService;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ManufacturerTypeAttributeRepository manufacturerTypeAttributeRepository;
    @Autowired
    private ManufacturerAttributeRepository manufacturerAttributeRepository;
    @Autowired
    private ManufacturerPartTypeAttributeRepository manufacturerPartTypeAttributeRepository;
    @Autowired
    private ManufacturerPartAttributeRepository manufacturerPartAttributeRepository;
    @Autowired
    private SupplierTypeAttributeRepository supplierTypeAttributeRepository;
    @Autowired
    private SupplierAttributeRepository supplierAttributeRepository;
    @Autowired
    private MessageSource messageSource;

    public String getManufacturerTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMManufacturer manufacturer = this.getManufacturerDetails(print);
                this.printDTO.setManufacturer(manufacturer);
                PrintDTO dtoForAttributes = this.getManufacturerAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            } else if (option.equals("Parts")) {
                this.printDTO.setMfrParts(manufacturerPartService.findAllPartsByMfr(print.getObjectId()));
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("sourcingData", this.printDTO);
        String templatePath = "sourcingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    private PLMManufacturer getManufacturerDetails(Print print) throws ParseException {
        PLMManufacturer manufacturer = manufacturerRepository.findOne(print.getObjectId());
        if (manufacturer != null) {
            Person person = personRepository.findOne(manufacturer.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(manufacturer.getModifiedBy());
            manufacturer.setTypeName(manufacturer.getMfrType().getName());
            manufacturer.setPhaseName(manufacturer.getLifeCyclePhase().getPhase());
            manufacturer.setCreatePerson(person.getFirstName());
            manufacturer.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return manufacturer;
    }


    public String getManufacturerPartTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMManufacturerPart manufacturerPart = this.getManufacturerPartDetails(print);
                this.printDTO.setManufacturerPart(manufacturerPart);
                PrintDTO dtoForAttributes = this.getManufacturerPartAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            } else if (option.equals("Where Used")) {
                this.printDTO.setWhereUsedItems(getPartsByMfrPart(print.getObjectId()));
            } else if (option.equals("Changes")) {
                this.printDTO.setMfrChanges(mcoService.findByAffectedItem(print.getObjectId()));
                this.printDTO.setMfrVarience(varianceService.getVariances(print.getObjectId()));
            } else if (option.equals("Quality")) {
                this.printDTO.setQcrs(qcrService.findByProblemPart(print.getObjectId()));
                this.printDTO.setNcrs(ncrService.findByProblemPart(print.getObjectId()));
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("sourcingData", this.printDTO);
        String templatePath = "sourcingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    private PLMManufacturerPart getManufacturerPartDetails(Print print) throws ParseException {
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(print.getObjectId());
        if (manufacturerPart != null) {
            Person person = personRepository.findOne(manufacturerPart.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(manufacturerPart.getModifiedBy());
            manufacturerPart.setTypeName(manufacturerPart.getMfrPartType().getName());
            manufacturerPart.setPhaseName(manufacturerPart.getLifeCyclePhase().getPhase());
            manufacturerPart.setCreatePerson(person.getFirstName());
            manufacturerPart.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return manufacturerPart;
    }

    @Transactional(readOnly = true)
    public List<PLMItemManufacturerPart> getPartsByMfrPart(Integer itemId) {
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(itemId);
        List<PLMItemManufacturerPart> plmItemManufacturerParts = itemManufacturerPartRepository.findByManufacturerPart(manufacturerPart);
        for (PLMItemManufacturerPart mfrPart : plmItemManufacturerParts) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(mfrPart.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            item.setRev(itemRevision);
            mfrPart.setItemMasterObject(item);
        }
        return plmItemManufacturerParts;
    }

    public String getSupplierTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMSupplier supplier = this.getSupplierDetails(print);
                this.printDTO.setSupplier(supplier);
                PrintDTO dtoForAttributes = this.getSupplierAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            } else if (option.equals("Contacts")) {
                this.printDTO.setContacts(supplierService.getSupplierContacts(print.getObjectId()));

            } else if (option.equals("Parts")) {
                this.printDTO.setSupParts(supplierService.getSupplierMfrParts(print.getObjectId()));
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("sourcingData", this.printDTO);
        String templatePath = "sourcingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);
        return exportHtmlData;
    }


    private PLMSupplier getSupplierDetails(Print print) throws ParseException {
        PLMSupplier supplier = supplierRepository.findOne(print.getObjectId());
        if (supplier != null) {
            Person person = personRepository.findOne(supplier.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(supplier.getModifiedBy());
            supplier.setTypeName(supplier.getSupplierType().getName());
            supplier.setCreatePerson(person.getFirstName());
            supplier.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return supplier;
    }

    private PrintDTO getManufacturerAttributes(Integer objectId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> manuPrintAttributesDTOs = new LinkedList<>();
        List<PrintAttributesDTO> masters = new ArrayList<>();
        PLMManufacturer manufacturer = manufacturerRepository.findOne(objectId);
        List<PLMManufacturerAttribute> manufacturerAttributes = manufacturerAttributeRepository.getByManufacturerId(manufacturer.getId());
        for (int i = 0; i < manufacturerAttributes.size(); i++) {
            PLMManufacturerAttribute masterAttr = manufacturerAttributes.get(i);
            PLMManufacturerTypeAttribute manufacturerTypeAttribute = manufacturerTypeAttributeRepository.findOne(masterAttr.getId().getAttributeDef());
            PrintAttributesDTO master = printAttributesDTO.getManufacturerObjectAttributes(manufacturerTypeAttribute, masterAttr);
            if (manufacturerTypeAttribute.getAttributeGroup() != null && manufacturerTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(manufacturerTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                manuPrintAttributesDTOs.add(master);
            }

        }
        Map<String, List<PrintAttributesDTO>> attributesMaterForGroup = this.printService.getAttributeByGroup(masters);
        dto.setItemMasterAttributes(manuPrintAttributesDTOs);
        dto.setGroupMasterAttributes(attributesMaterForGroup);
        return dto;
    }

    private PrintDTO getManufacturerPartAttributes(Integer objectId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> manuPrintAttributesDTOs = new LinkedList<>();
        List<PrintAttributesDTO> masters = new ArrayList<>();
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(objectId);
        List<PLMManufacturerPartAttribute> manufacturerAttributes = manufacturerPartAttributeRepository.getByManufacturerPartId(manufacturerPart.getId());
        for (int i = 0; i < manufacturerAttributes.size(); i++) {
            PLMManufacturerPartAttribute masterAttr = manufacturerAttributes.get(i);
            PLMManufacturerPartTypeAttribute manufacturerTypeAttribute = manufacturerPartTypeAttributeRepository.findOne(masterAttr.getId().getAttributeDef());
            PrintAttributesDTO master = printAttributesDTO.getManufacturerPartObjectAttributes(manufacturerTypeAttribute, masterAttr);
            if (manufacturerTypeAttribute.getAttributeGroup() != null && manufacturerTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(manufacturerTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                manuPrintAttributesDTOs.add(master);
            }
        }
        Map<String, List<PrintAttributesDTO>> attributesMaterForGroup = this.printService.getAttributeByGroup(masters);
        dto.setItemMasterAttributes(manuPrintAttributesDTOs);
        dto.setGroupMasterAttributes(attributesMaterForGroup);
        return dto;
    }

    private PrintDTO getSupplierAttributes(Integer objectId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> manuPrintAttributesDTOs = new LinkedList<>();
        List<PrintAttributesDTO> masters = new ArrayList<>();
        PLMSupplier supplier = supplierRepository.findOne(objectId);
        List<PLMSupplierAttribute> supplierAttributes = supplierAttributeRepository.getBySupplierId(supplier.getId());
        for (int i = 0; i < supplierAttributes.size(); i++) {
            PLMSupplierAttribute masterAttr = supplierAttributes.get(i);
            PLMSupplierTypeAttribute supplierTypeAttribute = supplierTypeAttributeRepository.findOne(masterAttr.getId().getAttributeDef());
            PrintAttributesDTO master = printAttributesDTO.getSupplierObjectAttributes(supplierTypeAttribute, masterAttr);
            if (supplierTypeAttribute.getAttributeGroup() != null && supplierTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(supplierTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                manuPrintAttributesDTOs.add(master);
            }
        }
        Map<String, List<PrintAttributesDTO>> attributesMaterForGroup = this.printService.getAttributeByGroup(masters);
        dto.setItemMasterAttributes(manuPrintAttributesDTOs);
        dto.setGroupMasterAttributes(attributesMaterForGroup);
        return dto;
    }

}