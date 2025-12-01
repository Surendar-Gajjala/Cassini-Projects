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
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.service.pm.ProjectItemReferenceService;
import com.cassinisys.plm.service.pm.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProjectPrintService {
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

    public String getProjectTemplate(Print print) throws ParseException {
        this.printDTO = new PrintDTO();
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMProject project = this.getProjectDetails(print);
                this.printDTO.setProject(project);
                PrintDTO dtoForAttributes = this.getProjectCustomAttribute(print.getObjectId());
                this.printDTO.setItemCustomAttributes(dtoForAttributes.getItemCustomAttributes());

            } else if (option.equals("Team")) {
                this.printDTO.setPersons(this.projectService.getAllProjectMembers(print.getObjectId()));

            } else if (option.equals("Plan")) {
                this.printDTO.setProjectPlan(this.getProjectPlan(print.getObjectId()));

            } else if (option.equals("Deliverables")) {
                this.getDeliverables(print);

            } else if (option.equals("Reference Items")) {
                this.printDTO.setProjectRefItems(this.projectItemReferenceService.getItemsByProject(print.getObjectId()));
            }
            else if (option.equals("Requirements")) {
                this.printDTO.setProjectRequirements(this.projectService.getProjectReqDocuments(print.getObjectId()));
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("projectData", this.printDTO);
        String templatePath = "projectTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    private String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private PLMProject getProjectDetails(Print print) throws ParseException {
        PLMProject plmProject = projectRepository.findOne(print.getObjectId());
        if (plmProject != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (plmProject.getPlannedStartDate() != null) {
                plmProject.setPlannedStartDatePrint(sdf.format(plmProject.getPlannedStartDate()));
            }
            if (plmProject.getPlannedFinishDate() != null) {
                plmProject.setPlannedFinishDatePrint(sdf.format(plmProject.getPlannedFinishDate()));
            }
            if (plmProject.getActualStartDate() != null) {
                plmProject.setActualStartDatePrint(sdf.format(plmProject.getActualStartDate()));
            }
            if (plmProject.getActualFinishDate() != null) {
                plmProject.setActualFinishDatePrint(sdf.format(plmProject.getActualFinishDate()));
            }


            Person person = personRepository.findOne(plmProject.getCreatedBy());
            plmProject.setPrintPerson(person.getFirstName());
            Person manager = personRepository.findOne(plmProject.getProjectManager());
            plmProject.setProjectManagerPrint(manager.getFirstName());
        }
        return plmProject;
    }

    private void getDeliverables(Print print) {
        PLMProjectDeliverableDto itemGlossaryDeliverables = this.projectService.getProjectDeliverableAndGlossaryDeliverble(print.getObjectId());
        PLMProjectDeliverableDto specDeliverables = this.projectService.getSpecificationDeliverables(print.getObjectId());
        PLMProjectDeliverableDto reqDeliverables = this.projectService.getRequirementDeliverables(print.getObjectId());
        this.printDTO.setItemDeliverables(itemGlossaryDeliverables.getItemDeliverables());
        this.printDTO.setGlossaryDeliverables(itemGlossaryDeliverables.getGlossaryDeliverables());
        this.printDTO.setSpecificationDeliverables(specDeliverables.getSpecificationDeliverables());
        this.printDTO.setRequirementDeliverables(reqDeliverables.getRequirementDeliverables());
    }

    private PrintDTO getProjectCustomAttribute(Integer ecoId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> itemCustomAttributes = new LinkedList<>();
        List<ObjectTypeAttribute> objTypeAttr = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf("PROJECT"));
        for (int k = 0; k < objTypeAttr.size(); k++) {
            ObjectTypeAttribute type = objTypeAttr.get(k);
            ObjectAttribute objAttr = objectAttributeRepository.findByObjectIdAndAttributeDefId(ecoId, type.getId());
            if (objAttr != null && type != null) {
                PrintAttributesDTO itemCust = printAttributesDTO.getItemCustomAttributes(type, objAttr);
                itemCustomAttributes.add(itemCust);
            }

        }
        dto.setItemCustomAttributes(itemCustomAttributes);
        return dto;
    }


    @Transactional(readOnly = true)
    public List<WBSDto> getProjectPlan(Integer projectId) throws ParseException {
        PLMProject project = projectRepository.findOne(projectId);
        List<WBSDto> wbsDtoList = new LinkedList<>();

        List<PLMWbsElement> rootWbsElements = wbsElementRepository.findByProjectAndParentIsNullOrderBySequenceNumberAsc(project);
        if (rootWbsElements.size() != 0) {
            for (PLMWbsElement wbsElement : rootWbsElements) {
                WBSDto wbsDto = new WBSDto();
                wbsDto.setName(wbsElement.getName());
                wbsDto.setDescription(wbsElement.getDescription());
                wbsDto.setLevel(0);
                wbsDto.setObjectType(wbsElement.getObjectType().toString());
                wbsDto.setProject(wbsElement.getProject());
                if (wbsElement.getPlannedStartDate() != null) {
                    wbsDto.setPlannedStartDatePrint(getDate(wbsElement.getPlannedStartDate()));

                }
                if (wbsElement.getPlannedFinishDate() != null) {
                    wbsDto.setPlannedFinishDatePrint(getDate(wbsElement.getPlannedFinishDate()));
                }


                wbsDtoList.add(wbsDto);
                List<PLMActivity> wbsActivities = activityRepository.findByWbsOrderBySequenceNumberAsc(wbsElement.getId());
                if (wbsActivities.size() > 0) {
                    for (PLMActivity activity : wbsActivities) {
                        WBSDto activityDto = new WBSDto();
                        activityDto.setName(activity.getName());
                        activityDto.setDescription(activity.getDescription());
                        activityDto.setLevel(wbsDto.getLevel() + 1);

                        if (activity.getPlannedStartDate() != null) {
                            activityDto.setPlannedStartDatePrint(getDate(activity.getPlannedStartDate()));
                        }
                        if (activity.getPlannedFinishDate() != null) {
                            activityDto.setPlannedFinishDatePrint(getDate(activity.getPlannedFinishDate()));
                        }

                        activityDto.setObjectType(activity.getObjectType().toString());
                        activityDto.setStatus(activity.getStatus().toString());
                        if (activity.getAssignedTo() != null) {
                            activityDto.setPerson(personRepository.findOne(activity.getAssignedTo()));
                        }
                        wbsDtoList.add(activityDto);
                        List<PLMTask> activityTasks = taskRepository.findByActivityOrderBySequenceNumberAsc(activity.getId());
                        for (PLMTask task : activityTasks) {
                            WBSDto taskDto = new WBSDto();
                            taskDto.setName(task.getName());
                            taskDto.setDescription(task.getDescription());
                            taskDto.setLevel(activityDto.getLevel() + 1);
                            if (task.getPlannedStartDate() != null) {
                                taskDto.setPlannedStartDatePrint(getDate(task.getPlannedStartDate()));
                            }
                            if (task.getPlannedFinishDate() != null) {
                                taskDto.setPlannedFinishDatePrint(getDate(task.getPlannedFinishDate()));
                            }
                            taskDto.setObjectType(task.getObjectType().toString());
                            taskDto.setStatus(task.getStatus().toString());
                            if (task.getAssignedTo() != null) {
                                taskDto.setPerson(personRepository.findOne(task.getAssignedTo()));
                            }
                            wbsDtoList.add(taskDto);

                        }

                    }
                }
                List<PLMMilestone> wbsMilestones = milestoneRepository.findByWbsOrderBySequenceNumberAsc(wbsElement.getId());
                if (wbsMilestones.size() > 0) {
                    for (PLMMilestone milestone : wbsMilestones) {
                        WBSDto milestoneDto = new WBSDto();
                        milestoneDto.setId(milestone.getId());
                        milestoneDto.setName(milestone.getName());
                        milestoneDto.setDescription(milestone.getDescription());
                        milestoneDto.setLevel(wbsDto.getLevel() + 1);
                        if (milestone.getPlannedFinishDate() != null) {
                            milestoneDto.setPlannedStartDatePrint(getDate(milestone.getPlannedFinishDate()));
                        }
                        if (milestone.getActualFinishDate() != null) {
                            milestoneDto.setPlannedFinishDatePrint(getDate(milestone.getActualFinishDate()));
                        }

                        milestoneDto.setObjectType(milestone.getObjectType().toString());
                        if (milestone.getAssignedTo() != null) {
                            milestoneDto.setPerson(personRepository.findOne(milestone.getAssignedTo()));
                        }
                        List<PLMActivity> finishedActivities = new ArrayList<>();
                        for (PLMActivity activity : wbsActivities) {
                            if (activity.getStatus().equals(ProjectActivityStatus.FINISHED)) {
                                finishedActivities.add(activity);
                            }
                        }
                        if (wbsActivities.size() == finishedActivities.size()) {
                            milestoneDto.setFinishMilestone(true);
                        } else {
                            milestoneDto.setFinishMilestone(false);
                        }
                        wbsDtoList.add(milestoneDto);
                    }
                }


            }

        }
        return wbsDtoList;
    }

}