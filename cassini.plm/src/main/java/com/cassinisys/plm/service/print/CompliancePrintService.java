package com.cassinisys.plm.service.print;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.plm.config.MessageResolverMethod;
import com.cassinisys.plm.model.dto.Print;
import com.cassinisys.plm.model.dto.print.PrintAttributesDTO;
import com.cassinisys.plm.model.dto.print.PrintDTO;
import com.cassinisys.plm.model.mfr.PLMSupplierContact;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.repo.mfr.SupplierContactRepository;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.pgc.*;
import com.cassinisys.plm.service.pgc.DeclarationService;
import com.cassinisys.plm.service.pgc.PGCSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/*
* Created by GSR Cassini on 26-12-2020.
* */
@Service
public class CompliancePrintService {
    private PrintDTO printDTO;
    @Autowired
    private PrintService printService;
    @Autowired
    private PGCSubstanceRepository substanceRepository;
    @Autowired
    private PGCSpecificationRepository specificationRepository;
    @Autowired
    private PGCDeclarationRepository declarationRepository;
    @Autowired
    private PGCObjectAttributeRepository pgcObjectAttributeRepository;
    @Autowired
    private PGCObjectTypeAttributeRepository pgcObjectTypeAttributeRepository;
    @Autowired
    private PrintAttributesDTO printAttributesDTO;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierContactRepository supplierContactRepository;
    @Autowired
    private PGCSpecificationService specificationService;
    @Autowired
    private DeclarationService declarationService;
    @Autowired
    private MessageSource messageSource;

    public String getSubstanceTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PGCSubstance substance = this.getSubstanceDetails(print);
                this.printDTO.setSubstance(substance);
                PrintDTO dtoForAttributes = this.getPGCObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("complianceData", this.printDTO);
        String templatePath = "complianceTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    public String getSpecificationTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PGCSpecification specification = this.getSpecificationDetails(print);
                this.printDTO.setSpecification(specification);
                PrintDTO dtoForAttributes = this.getPGCObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            } else if (option.equals("Substances")) {
                this.printDTO.setSubstanceList(specificationService.getAllSpecSubstances(print.getObjectId()));
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("complianceData", this.printDTO);
        String templatePath = "complianceTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private PGCSpecification getSpecificationDetails(Print print) throws ParseException {
        PGCSpecification specification = specificationRepository.findOne(print.getObjectId());
        if (specification != null) {
            Person person = personRepository.findOne(specification.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(specification.getModifiedBy());
            specification.setCreatePerson(person.getFirstName());
            specification.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return specification;
    }


    public String getDeclarationTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PGCDeclaration declaration = this.getDeclarationDetails(print);
                this.printDTO.setDeclaration(declaration);
                PrintDTO dtoForAttributes = this.getPGCObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());
            }
            if (option.equals("Parts")) {
                this.printDTO.setDeclarationParts(declarationService.getAllDeclarationParts(print.getObjectId()));
            }
            if (option.equals("Specifications")) {
                this.printDTO.setDeclarationSpecifications(declarationService.getDeclarationSpecifications(print.getObjectId()));
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("complianceData", this.printDTO);
        String templatePath = "complianceTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private PGCDeclaration getDeclarationDetails(Print print) throws ParseException {
        PGCDeclaration declaration = declarationRepository.findOne(print.getObjectId());
        if (declaration != null) {
            Person person = personRepository.findOne(declaration.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(declaration.getModifiedBy());
            declaration.setCreatePerson(person.getFirstName());
            declaration.setModifiedPerson(modifiedPerson.getFirstName());
            if (declaration.getSupplier() != null) {
                declaration.setSupplierName(supplierRepository.findOne(declaration.getSupplier()).getName());
            }
            if (declaration.getContact() != null) {
                PLMSupplierContact supplierContact = supplierContactRepository.findOne(declaration.getContact());
                Person person1 = personRepository.findOne(supplierContact.getContact());
                declaration.setSupplierContactName(person1.getFullName());
            }
        }
        return declaration;
    }

    private PGCSubstance getSubstanceDetails(Print print) throws ParseException {
        PGCSubstance substance = substanceRepository.findOne(print.getObjectId());
        if (substance != null) {
            Person person = personRepository.findOne(substance.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(substance.getModifiedBy());
            substance.setCreatePerson(person.getFirstName());
            substance.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return substance;
    }

    private PrintDTO getPGCObjectTypeAttributes(Integer objectId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> mesAttributes = new LinkedList<>();
        List<PrintAttributesDTO> masters = new ArrayList<>();
        List<PGCObjectAttribute> masterAttributes = pgcObjectAttributeRepository.findByObjectId(objectId);
        for (int i = 0; i < masterAttributes.size(); i++) {
            PGCObjectAttribute masterAttr = masterAttributes.get(i);
            PGCObjectTypeAttribute itemTypeAttribute = pgcObjectTypeAttributeRepository.findOne(masterAttr.getId().getAttributeDef());
            PrintAttributesDTO master = printAttributesDTO.getPgcObjectAttributes(itemTypeAttribute, masterAttr);
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