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
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.service.mes.AssemblyLineService;
import com.cassinisys.plm.service.mes.MachineService;
import com.cassinisys.plm.service.mes.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
* Created by Suresh Cassini on 24-12-2020.
* */
@Service
public class ManufacturingPrintService {
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
    private MESPlantRepository plantRepo;
    @Autowired
    private PlantService plantService;
    @Autowired
    private MESAssemblyLineRepository assemblyLineRepository;
    @Autowired
    private AssemblyLineService assemblyLineService;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;
    @Autowired
    private MachineService machineService;
    @Autowired
    private MESMachineRepository machineRepository;
    @Autowired
    private ManufacturerDataRepository manufacturerDataRepository;
    @Autowired
    private MESEquipmentRepository equipmentRepository;
    @Autowired
    private MESInstrumentRepository instrumentRepository;
    @Autowired
    private MESToolRepository toolRepo;
    @Autowired
    private MESJigsFixtureRepository jigsFixtureRepository;
    @Autowired
    private MESMaterialRepository materialRepository;
    @Autowired
    private MESManpowerRepository manpowerRepository;
    @Autowired
    private MESShiftRepository shiftRepo;
    @Autowired
    private MESOperationRepository operationReposotory;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private MessageSource messageSource;

    private String getResourceImage(byte[] thumbnail) {
        String baseString = "";
        byte[] imgBytesAsBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(thumbnail);
        String imgDataAsBase64 = new String(imgBytesAsBase64);
        baseString = "data:image/png;base64," + imgDataAsBase64;
        return baseString;
    }

    public String getPlantTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESPlant plant = this.getPlantDetails(print);
                this.printDTO.setPlant(plant);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    public String getAssemblyLineTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESAssemblyLine assemblyLine = this.getAssemblyLineDetails(print);
                this.printDTO.setAssemblyLine(assemblyLine);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            } else if (option.equals("Work Centers")) {
                this.printDTO.setWorkCenters(assemblyLineService.getAssemblyLineWorkCenters(print.getObjectId()));
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    public String getWorkcenterTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESWorkCenter workCenter = this.getWorkCenterDetails(print);
                this.printDTO.setWorkCenter(workCenter);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
            if (option.equals("Resources")) {

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MESWorkCenter getWorkCenterDetails(Print print) throws ParseException {
        MESWorkCenter workCenter = workCenterRepository.findOne(print.getObjectId());
        if (workCenter != null) {
            Person person = personRepository.findOne(workCenter.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(workCenter.getModifiedBy());
            workCenter.setCreatePerson(person.getFirstName());
            workCenter.setPlantName(plantRepo.findOne(workCenter.getPlant()).getName());
            workCenter.setTypeName(workCenter.getType().getName());
            workCenter.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return workCenter;
    }


    public String getMachineTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESMachine machine = this.getMachineDetails(print);
                this.printDTO.setMachine(machine);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }

        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MESMachine getMachineDetails(Print print) throws ParseException {
        MESMachine machine = machineRepository.findOne(print.getObjectId());
        if (machine != null) {
            if (machine.getImage() != null) {
                machine.setImageValue(getResourceImage(machine.getImage()));
            } else {
                machine.setImageValue(null);
            }
            Person person = personRepository.findOne(machine.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(machine.getModifiedBy());
            machine.setCreatePerson(person.getFirstName());
            machine.setWorkCenterName(workCenterRepository.findOne(machine.getWorkCenter()).getName());
            machine.setTypeName(machine.getType().getName());
            machine.setManufacturerData(manufacturerDataRepository.findOne(machine.getId()));
            machine.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return machine;
    }

    public String getEquipmentTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESEquipment equipment = this.getEquipmentDetails(print);
                this.printDTO.setEquipment(equipment);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MESEquipment getEquipmentDetails(Print print) throws ParseException {
        MESEquipment equipment = equipmentRepository.findOne(print.getObjectId());
        if (equipment != null) {
            if (equipment.getImage() != null) {
                equipment.setImageValue(getResourceImage(equipment.getImage()));
            } else {
                equipment.setImageValue(null);
            }
            Person person = personRepository.findOne(equipment.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(equipment.getModifiedBy());
            equipment.setCreatePerson(person.getFirstName());
            equipment.setTypeName(equipment.getType().getName());
            equipment.setManufacturerData(manufacturerDataRepository.findOne(equipment.getId()));
            equipment.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return equipment;
    }


    public String getInstrumentTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESInstrument instrument = this.getInstrumentDetails(print);
                this.printDTO.setInstrument(instrument);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
            if (option.equals("Maintenance")) {


            }
            if (option.equals("Related Items")) {


            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MESInstrument getInstrumentDetails(Print print) throws ParseException {
        MESInstrument instrument = instrumentRepository.findOne(print.getObjectId());
        if (instrument != null) {
            if (instrument.getImage() != null) {
                instrument.setImageValue(getResourceImage(instrument.getImage()));
            } else {
                instrument.setImageValue(null);
            }
            Person person = personRepository.findOne(instrument.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(instrument.getModifiedBy());
            instrument.setCreatePerson(person.getFirstName());
            instrument.setTypeName(instrument.getType().getName());
            instrument.setManufacturerData(manufacturerDataRepository.findOne(instrument.getId()));
            instrument.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return instrument;
    }

    public String getToolTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESTool tool = this.getToolDetails(print);
                this.printDTO.setTool(tool);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MESTool getToolDetails(Print print) throws ParseException {
        MESTool tool = toolRepo.findOne(print.getObjectId());
        if (tool != null) {
            if (tool.getImage() != null) {
                tool.setImageValue(getResourceImage(tool.getImage()));
            } else {
                tool.setImageValue(null);
            }
            Person person = personRepository.findOne(tool.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(tool.getModifiedBy());
            tool.setCreatePerson(person.getFirstName());
            tool.setTypeName(tool.getType().getName());
            tool.setManufacturerData(manufacturerDataRepository.findOne(tool.getId()));
            tool.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return tool;
    }

    public String getJigsFixtureTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESJigsFixture jigsFixture = this.getJigsFixtureDetails(print);
                this.printDTO.setJigsFixture(jigsFixture);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MESJigsFixture getJigsFixtureDetails(Print print) throws ParseException {
        MESJigsFixture jigsFixture = jigsFixtureRepository.findOne(print.getObjectId());
        if (jigsFixture != null) {
            if (jigsFixture.getImage() != null) {
                jigsFixture.setImageValue(getResourceImage(jigsFixture.getImage()));
            } else {
                jigsFixture.setImageValue(null);
            }
            Person person = personRepository.findOne(jigsFixture.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(jigsFixture.getModifiedBy());
            jigsFixture.setCreatePerson(person.getFirstName());
            jigsFixture.setTypeName(jigsFixture.getType().getName());
            jigsFixture.setManufacturerData(manufacturerDataRepository.findOne(jigsFixture.getId()));
            jigsFixture.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return jigsFixture;
    }


    public String getMaterialTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESMaterial material = this.getMaterialDetails(print);
                this.printDTO.setMaterial(material);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("selectedOptions", print.getOptions());
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MESMaterial getMaterialDetails(Print print) throws ParseException {
        MESMaterial material = materialRepository.findOne(print.getObjectId());
        if (material != null) {
            if (material.getImage() != null) {
                material.setImageValue(getResourceImage(material.getImage()));
            } else {
                material.setImageValue(null);
            }
            Measurement measurement = measurementRepository.findOne(material.getQom());
            MeasurementUnit unit = measurementUnitRepository.findOne(material.getUom());
            material.setQomName(measurement.getName());
            material.setUomName(unit.getName());
            Person person = personRepository.findOne(material.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(material.getModifiedBy());
            material.setCreatePerson(person.getFirstName());
            material.setTypeName(material.getType().getName());
            material.setManufacturerData(manufacturerDataRepository.findOne(material.getId()));
            material.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return material;
    }


    public String getShiftTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESShift shift = this.getShiftDetails(print);
                this.printDTO.setShift(shift);

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    private MESShift getShiftDetails(Print print) throws ParseException {
        MESShift shift = shiftRepo.findOne(print.getObjectId());
        if (shift != null) {
            Person person = personRepository.findOne(shift.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(shift.getModifiedBy());
            shift.setCreatePerson(person.getFirstName());
            shift.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return shift;
    }


    public String getOperationTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESOperation operation = this.getOperationDetails(print);
                this.printDTO.setOperation(operation);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MESOperation getOperationDetails(Print print) throws ParseException {
        MESOperation operation = operationReposotory.findOne(print.getObjectId());
        if (operation != null) {
            Person person = personRepository.findOne(operation.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(operation.getModifiedBy());
            operation.setCreatePerson(person.getFirstName());
            operation.setTypeName(operation.getType().getName());
            operation.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return operation;
    }

    public String getManpowerTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESManpower manpower = this.getManpowerDetails(print);
                this.printDTO.setManpower(manpower);
                PrintDTO dtoForAttributes = this.getMesObjectTypeAttributes(print.getObjectId());
                this.printDTO.setItemMasterAttributes(dtoForAttributes.getItemMasterAttributes());
                this.printDTO.setGroupMasterAttributes(dtoForAttributes.getGroupMasterAttributes());

            }
            if (option.equals("Maintenance")) {


            }

            if (option.equals("Related Items")) {


            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("manufacturingData", this.printDTO);
        String templatePath = "manufacturingTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private MESManpower getManpowerDetails(Print print) throws ParseException {
        MESManpower manpower = manpowerRepository.findOne(print.getObjectId());
        if (manpower != null) {
            Person person = personRepository.findOne(manpower.getCreatedBy());
            // Person manpowerPerson = personRepository.findOne(manpower.getPerson());
            Person modifiedPerson = personRepository.findOne(manpower.getModifiedBy());
            manpower.setCreatePerson(person.getFirstName());
            manpower.setTypeName(manpower.getType().getName());
            manpower.setModifiedPerson(modifiedPerson.getFirstName());
            // manpower.setPersonName(manpowerPerson.getFirstName());
        }
        return manpower;
    }


    private String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private MESPlant getPlantDetails(Print print) throws ParseException {
        MESPlant plant = plantRepo.findOne(print.getObjectId());
        if (plant != null) {
            Person person = personRepository.findOne(plant.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(plant.getModifiedBy());
            plant.setCreatePerson(person.getFirstName());
            Person manager = personRepository.findOne(plant.getPlantPerson());
            plant.setPerson(manager.getFirstName());
            plant.setTypeName(plant.getType().getName());
            plant.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return plant;
    }

    private MESAssemblyLine getAssemblyLineDetails(Print print) throws ParseException {
        MESAssemblyLine assemblyLine = assemblyLineRepository.findOne(print.getObjectId());
        if (assemblyLine != null) {
            Person person = personRepository.findOne(assemblyLine.getCreatedBy());
            Person modifiedPerson = personRepository.findOne(assemblyLine.getModifiedBy());
            assemblyLine.setCreatePerson(person.getFirstName());
            assemblyLine.setPlantName(plantRepo.findOne(assemblyLine.getPlant()).getName());
            assemblyLine.setModifiedPerson(modifiedPerson.getFirstName());
        }
        return assemblyLine;
    }


    private PrintDTO getMesObjectTypeAttributes(Integer objectId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> mesAttributes = new LinkedList<>();
        List<PrintAttributesDTO> masters = new ArrayList<>();
        List<MESObjectAttribute> masterAttributes = mesObjectAttributeRepository.findByObjectId(objectId);
        for (int i = 0; i < masterAttributes.size(); i++) {
            MESObjectAttribute masterAttr = masterAttributes.get(i);
            MESObjectTypeAttribute itemTypeAttribute = mesObjectTypeAttributeRepository.findOne(masterAttr.getId().getAttributeDef());
            PrintAttributesDTO master = printAttributesDTO.getMesObjectAttributes(itemTypeAttribute, masterAttr);
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