package com.cassinisys.plm.service.print;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.plm.config.MessageResolverMethod;
import com.cassinisys.plm.model.dto.Print;
import com.cassinisys.plm.model.dto.print.PrintAttributesDTO;
import com.cassinisys.plm.model.dto.print.PrintDTO;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PLMProgram;
import com.cassinisys.plm.model.pm.PLMProgramResource;
import com.cassinisys.plm.model.pm.ProgramProjectDto;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.service.pm.ProgramService;
import com.cassinisys.plm.service.pm.ProjectItemReferenceService;
import com.cassinisys.plm.service.pm.ProjectService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProgramPrintService {
    private PrintDTO printDTO;
    @Autowired
    private PrintService printService;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private PrintAttributesDTO printAttributesDTO;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private WbsElementRepository wbsElementRepository;
    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ProjectItemReferenceService projectItemReferenceService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ProgramService programService;
    @Autowired
    private ObjectFileService objectFileService;

    public String getProgramTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMProgram project = this.getProgramDetails(print);
                this.printDTO.setProgram(project);
                PrintDTO dtoForAttributes = this.getProgramCustomAttribute(print.getObjectId());
                this.printDTO.setItemCustomAttributes(dtoForAttributes.getItemCustomAttributes());

            } else if (option.equals("Resources")) {
                List<PLMProgramResource> programResources = this.programService.getProgramResources(print.getObjectId());
                programResources.forEach(programResource -> {
                    Person person = personRepository.findOne(programResource.getPerson());
                    programResource.setPersonName(person.getFullName());
                    programResource.setPhoneMobile(person.getPhoneMobile());
                    programResource.setEmail(person.getEmail());
                }); 
                this.printDTO.setResources(programResources);

            } else if (option.equals("Projects")) {
                List<ProgramProjectDto> programProjectDtos = this.programService.getProgramProjects(print.getObjectId());
                programProjectDtos.forEach(programProjectDto -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    if (programProjectDto.getPlannedStartDate() != null) {
                        programProjectDto.setPStartDate(sdf.format(programProjectDto.getPlannedStartDate()));
                    }
                    if (programProjectDto.getPlannedFinishDate() != null) {
                        programProjectDto.setPFinishDate(sdf.format(programProjectDto.getPlannedFinishDate()));
                    }
                    if (programProjectDto.getActualStartDate() != null) {
                        programProjectDto.setAStartDate(sdf.format(programProjectDto.getActualStartDate()));
                    }
                    if (programProjectDto.getActualFinishDate() != null) {
                        programProjectDto.setAFinishDate(sdf.format(programProjectDto.getActualFinishDate()));
                    }
                    this.printDTO.getProgramProjects().add(programProjectDto);
                    programProjectDto.getChildren().forEach(child -> {
                        if (child.getPlannedStartDate() != null) {
                            child.setPStartDate(sdf.format(child.getPlannedStartDate()));
                        }
                        if (child.getPlannedFinishDate() != null) {
                            child.setPFinishDate(sdf.format(child.getPlannedFinishDate()));
                        }
                        if (child.getActualStartDate() != null) {
                            child.setAStartDate(sdf.format(child.getActualStartDate()));
                        }
                        if (child.getActualFinishDate() != null) {
                            child.setAFinishDate(sdf.format(child.getActualFinishDate()));
                        }
                        child.setLevel(1);
                        this.printDTO.getProgramProjects().add(child);
                    });
                });

            } else if (option.equals("Files")) {
                this.printDTO.setFileDto(this.objectFileService.getObjectFiles(print.getObjectId(), PLMObjectType.PROGRAM,false));
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("programData", this.printDTO);
        String templatePath = "programTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    private PLMProgram getProgramDetails(Print print) throws ParseException {
        PLMProgram plmProject = programRepository.findOne(print.getObjectId());
        if (plmProject != null) {
            Person person = personRepository.findOne(plmProject.getCreatedBy());
            plmProject.setCreatedByName(person.getFirstName());
            Person manager = personRepository.findOne(plmProject.getProgramManager());
            plmProject.setProgramManagerName(manager.getFirstName());
        }
        return plmProject;
    }

    private PrintDTO getProgramCustomAttribute(Integer programId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> itemCustomAttributes = new LinkedList<>();
        List<ObjectTypeAttribute> objTypeAttr = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf("PROGRAM"));
        for (int k = 0; k < objTypeAttr.size(); k++) {
            ObjectTypeAttribute type = objTypeAttr.get(k);
            ObjectAttribute objAttr = objectAttributeRepository.findByObjectIdAndAttributeDefId(programId, type.getId());
            if (objAttr != null && type != null) {
                PrintAttributesDTO itemCust = printAttributesDTO.getItemCustomAttributes(type, objAttr);
                itemCustomAttributes.add(itemCust);
            }

        }
        dto.setItemCustomAttributes(itemCustomAttributes);
        return dto;
    }

}