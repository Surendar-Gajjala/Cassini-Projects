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
import com.cassinisys.plm.model.mes.MESBOP;
import com.cassinisys.plm.model.mes.MESBOPRevision;
import com.cassinisys.plm.model.mes.MESMBOM;
import com.cassinisys.plm.model.mes.MESMBOMRevision;
import com.cassinisys.plm.model.mes.dto.BOPDto;
import com.cassinisys.plm.model.mes.dto.BOPRouteDto;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PLMProgram;
import com.cassinisys.plm.model.pm.PLMProgramResource;
import com.cassinisys.plm.model.pm.ProgramProjectDto;
import com.cassinisys.plm.repo.mes.BOPRevisionRepository;
import com.cassinisys.plm.repo.mes.MESBOPRepository;
import com.cassinisys.plm.repo.mes.MESMBOMRepository;
import com.cassinisys.plm.repo.mes.MESMBOMRevisionRepository;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.service.mes.BOPService;
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
public class BopPrintService {
    private PrintDTO printDTO;
    @Autowired
    private PrintService printService;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private PrintAttributesDTO printAttributesDTO;
    @Autowired
    private MESBOPRepository bopRepository;
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
    @Autowired
    private BOPRevisionRepository bopRevisionRepository;
    @Autowired
    private MESMBOMRevisionRepository mbomRevisionRepository;  
    @Autowired
    private BOPService bopService; 


    public String getBopTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                MESBOPRevision bopRevision = bopRevisionRepository.findOne(print.getObjectId());
                MESBOP bop = bopRepository.findOne(bopRevision.getMaster());
                BOPDto bopDto = bopService.convertBopToDto(bop);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    if (bopDto.getCreatedDate() != null) {
                        bopDto.setCreatedDateString(sdf.format(bopDto.getCreatedDate()));
                    }
                    if (bopDto.getModifiedDate() != null) {
                        bopDto.setModifiedDateString(sdf.format(bopDto.getModifiedDate()));
                    }
                this.printDTO.setBopDto(bopDto);
                PrintDTO dtoForAttributes = this.getBopCustomAttribute(print.getObjectId());
                this.printDTO.setItemCustomAttributes(dtoForAttributes.getItemCustomAttributes());

            } else if (option.equals("Route")) {
                List<BOPRouteDto> bopRouteDtos = this.bopService.getBopRoutes(print.getObjectId());
                bopRouteDtos.forEach(bopRouteDto -> {
                    this.printDTO.getBopRoutes().add(bopRouteDto);
                    bopRouteDto.getChildren().forEach(child -> {
                        child.setLevel(1);
                        this.printDTO.getBopRoutes().add(child);
                    });
               }); 
            }    
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("bopData", this.printDTO);
        String templatePath = "bopTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private PrintDTO getBopCustomAttribute(Integer bopId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> itemCustomAttributes = new LinkedList<>();
        List<ObjectTypeAttribute> objTypeAttr = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf("BOP"));
        for (int k = 0; k < objTypeAttr.size(); k++) {
            ObjectTypeAttribute type = objTypeAttr.get(k);
            ObjectAttribute objAttr = objectAttributeRepository.findByObjectIdAndAttributeDefId(bopId, type.getId());
            if (objAttr != null && type != null) {
                PrintAttributesDTO itemCust = printAttributesDTO.getItemCustomAttributes(type, objAttr);
                itemCustomAttributes.add(itemCust);
            }

        }
        dto.setItemCustomAttributes(itemCustomAttributes);
        return dto;
    }

}