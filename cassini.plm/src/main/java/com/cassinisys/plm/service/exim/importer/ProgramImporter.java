package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.repo.pm.MilestoneRepository;
import com.cassinisys.plm.repo.pm.PLMActivityRepository;
import com.cassinisys.plm.repo.pm.ProgramProjectRepository;
import com.cassinisys.plm.repo.pm.ProgramRepository;
import com.cassinisys.plm.repo.pm.ProgramResourceRepository;
import com.cassinisys.plm.repo.pm.ProjectMemberRepository;
import com.cassinisys.plm.repo.pm.ProjectRepository;
import com.cassinisys.plm.repo.pm.TaskRepository;
import com.cassinisys.plm.repo.pm.WbsElementRepository;
import com.cassinisys.plm.service.pm.PMObjectTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Created by Lenovo on 12-11-2021.
 */
@Service
@Scope("prototype")
public class ProgramImporter {
    
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private WbsElementRepository wbsElementRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PMObjectTypeService pmObjectTypeService;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ProgramProjectRepository programProjectRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private ProgramResourceRepository programResourceRepository;

    public static ConcurrentMap<String, PMObjectType> rootPmObjectTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PMObjectType> pmObjectsMapByPath = new ConcurrentHashMap<>();
    public static final String PROGRAM = "Program";
    public static final String GROUP = "Group";
    public static final String PROJECT = "Project";
    public static final String PHASE = "Phase";
    public static final String ACTIVITY = "Activity";
    public static final String TASK = "Task";
    public static final String MILESTONE = "Milestone";

    public void loadProjectClassificationTree() {
        rootPmObjectTypesMap = new ConcurrentHashMap<>();
        pmObjectsMapByPath = new ConcurrentHashMap<>();
        List<PMObjectType> rootTypes = pmObjectTypeService.getTypeTree();
        for (PMObjectType rootType : rootTypes) {
            rootPmObjectTypesMap.put(rootType.getName(), rootType);
            pmObjectsMapByPath.put(rootType.getName(), rootType);
        }
    }


    private PMObjectType getProjectTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PMObjectType pmObjectType = rootPmObjectTypesMap.get(name);
            if (pmObjectType != null) {
                return  pmObjectType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return pmObjectType;
            }

        } else {
            name = path;
            return rootPmObjectTypesMap.get(name);
        }
    }

    public PMObjectType getProjectType(String typePath) {
        PMObjectType pmObjectType = pmObjectsMapByPath.get(typePath);
        if (pmObjectType == null) {
            pmObjectType = getProjectTypeByPath(typePath);
            if (pmObjectType == null) {
                pmObjectType = createProjectTypeByPath(null, typePath);
            }

            pmObjectsMapByPath.put(typePath, pmObjectType);
        }
        return pmObjectType;
    }

    private AutoNumber getDefaultProjectNumberSource() {
        return autoNumberService.getByName("Default Project Number Source");
    }


    private PMObjectType createProjectTypeByPath(PMObjectType parentType, String path) {
        String name;
        int index = path.indexOf('/');
        String restOfPath = null;
        if (index != -1) {
            name = path.substring(0, index);
            restOfPath = path.substring(index + 1);
        } else {
            name = path;
        }

        if (parentType == null) {
            parentType = rootPmObjectTypesMap.get(name);
            if (parentType == null) {
                parentType = new PMObjectType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setType(PMType.PROJECT);
                parentType.setAutoNumberSource(getDefaultProjectNumberSource());
                parentType = pmObjectTypeService.createProjectType(parentType);
                rootPmObjectTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createProjectTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PMObjectType childPmType = new PMObjectType();
            childPmType.setParent(parentType.getId());
            childPmType.setName(name);
            childPmType.setDescription(name);
            childPmType.setType(PMType.PROJECT);
            childPmType.setAutoNumberSource(getDefaultProjectNumberSource());
            childPmType = pmObjectTypeService.createProjectType(childPmType);
            parentType.getChildren().add(childPmType);
            if (restOfPath != null) {
                return parentType = createProjectTypeByPath(childPmType, restOfPath);
            } else {
                return childPmType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createProjectTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    public void importProjects(TableData tableData) throws ParseException {
        this.loadProjectClassificationTree();
        Map<String, PLMProgram> dbProgramMap = new LinkedHashMap();
        Map<String, PLMProgramProject> dbProgamProjectsMap = new LinkedHashMap();
        Map<String, PLMProject> dbProjectMap = new LinkedHashMap();
        Map<String, PLMWbsElement> dbPhaseMap = new LinkedHashMap();
        Map<String, PLMActivity> dbActivityMap = new LinkedHashMap();
        Map<String, PLMTask> dbTaskMap = new LinkedHashMap();
        Map<String, PLMMilestone> dbMilestoneMap = new LinkedHashMap();
        Map<String, PMObjectType> dbProjectTypeMap = new LinkedHashMap();
        Map<String, PLMProjectMember> dbProjectMembersMap = new LinkedHashMap();
        Map<String, PLMProgramResource> dbProgramResourceMap = new LinkedHashMap();
        List<PLMProgram> programs = programRepository.findAll();
        List<PLMProgramProject> programProjects = programProjectRepository.findAll();
        List<PLMProject> projects = projectRepository.findAll();
        List<PLMWbsElement> phases = wbsElementRepository.findAll();
        List<PMObjectType> projectTypes = pmObjectTypeService.getTypeTree();
        List<PLMActivity> activities = activityRepository.findAll();
        List<PLMMilestone> milestones = milestoneRepository.findAll();
        List<PLMProjectMember> projectMembers = projectMemberRepository.findAll();
        List<PLMProgramResource> programResources = programResourceRepository.findAll();
        List<PLMTask> tasks = taskRepository.findAll();
        dbProgramMap = programs.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        dbProgamProjectsMap = programProjects.stream().collect(Collectors.toMap(x -> x.getName() + x.getProgram() + x.getProject(), x -> x));
        dbProjectMap = projects.stream().collect(Collectors.toMap(x -> x.getName() + x.getProgram() , x -> x));
        dbPhaseMap = phases.stream().collect(Collectors.toMap(x -> x.getName() + x.getProject().getId(), x -> x));
        dbActivityMap = activities.stream().collect(Collectors.toMap(x -> x.getName() + x.getWbs(), x -> x));
        dbTaskMap = tasks.stream().collect(Collectors.toMap(x -> x.getName() + x.getActivity(), x -> x));
        dbMilestoneMap = milestones.stream().collect(Collectors.toMap(x -> x.getName() + x.getWbs(), x -> x));
        dbProjectTypeMap = projectTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        dbProjectMembersMap = projectMembers.stream().collect(Collectors.toMap(x -> x.getProject().toString() + x.getPerson().toString(), x -> x));
        dbProgramResourceMap = programResources.stream().collect(Collectors.toMap(x -> x.getProgram().toString() + x.getPerson().toString(), x -> x));
        createProjects(tableData, dbProgramMap, dbProgamProjectsMap, dbProjectMap, dbPhaseMap, dbActivityMap, 
        dbTaskMap, dbMilestoneMap, dbProjectTypeMap, dbProjectMembersMap, dbProgramResourceMap);
    }

    private void createMembers(String type, PLMProgram program, PLMProject project,String team
    , Map<String, PLMProjectMember> dbProjectMembersMap, Map<String, PLMProgramResource> dbProgramResourceMap) {
        if(type.trim().equals(PROJECT)) {
            List<PLMProjectMember> projectMembers = new LinkedList<>();
            PLMProjectMember plmProjectMember = new PLMProjectMember();
            String[] teamMembers = team.split(",");
            for(String member: teamMembers) {
                if(member.contains(";")) {
                    String[] roleMember = member.split(";");
                    Person projectManager1 = getPerson(roleMember[0], "team member");
                    if(projectManager1 != null) plmProjectMember.setPerson(projectManager1.getId());
                    plmProjectMember.setRole(roleMember[1]);
                } else {
                    Person projectManager1 = getPerson(member, "team member");
                    if(projectManager1 != null) plmProjectMember.setPerson(projectManager1.getId());
                }
                plmProjectMember.setProject(project.getId());
            }
            if(dbProjectMembersMap.get(plmProjectMember.getProject().toString() + plmProjectMember.getPerson().toString()) == null) {
                projectMembers.add(plmProjectMember);
            }
            if(projectMembers.size() > 0) {
                projectMemberRepository.save(projectMembers);
            }
       }

       if(type.trim().equals(PROGRAM)) {
        List<PLMProgramResource> resources = new LinkedList<>();
        PLMProgramResource plmProgramResource = new PLMProgramResource();
        String[] resourceMembers = team.split(",");
        for(String member: resourceMembers) {
            if(member.contains(";")) {
                String[] roleMember = member.split(";");
                Person projectManager1 = getPerson(roleMember[0], "resource member");
                if(projectManager1 != null) plmProgramResource.setPerson(projectManager1.getId());
                plmProgramResource.setPersonName(roleMember[0]);
                plmProgramResource.setRole(roleMember[1]);
            } else {
                Person projectManager1 = getPerson(member, "resource member");
                if(projectManager1 != null) plmProgramResource.setPerson(projectManager1.getId());
                plmProgramResource.setPersonName(member);
            }
            plmProgramResource.setProgram(program.getId());
        }
        if(dbProgramResourceMap.get(plmProgramResource.getProgram().toString() + plmProgramResource.getPerson().toString()) == null) {
            resources.add(plmProgramResource);
        }
        if(resources.size() > 0) {
            programResourceRepository.save(resources);
        }
   }
    }

    private void createProjects(TableData tableData, Map<String, PLMProgram> dbProgramMap, Map<String, PLMProgramProject> dbProgamProjectsMap, Map<String, PLMProject> dbProjectMap, 
    Map<String, PLMWbsElement> dbPhaseMap, Map<String, PLMActivity> dbActivityMap, Map<String, PLMTask> dbTaskMap, Map<String, PLMMilestone> dbMilestoneMap,Map<String, PMObjectType> dbProjectTypeMap,
    Map<String, PLMProjectMember> dbProjectMembersMap, Map<String, PLMProgramResource> dbProgramResourceMap) {
        PLMProgram program = new PLMProgram();
        PLMProgramProject programProject = new PLMProgramProject();
        PLMProject project = new PLMProject();
        PLMWbsElement phase = new PLMWbsElement();
        PLMActivity activity = new PLMActivity();
        int size = tableData.getRows().size();
        if(size > 0) {
            for (int i = 0; i < size; i++) {
                RowData stringListHashMap = tableData.getRows().get(i);
                String type = stringListHashMap.get("Type");
                if(type != null && type.trim() != "") {
                    if(type.trim().equals(PROGRAM)) {
                        String typePath = stringListHashMap.get("Type Path");
                        if (isFiledsValid(type, i, stringListHashMap)) {
                                program = programRepository.save(createProgramByPath(i, stringListHashMap.get("Name").trim(), typePath, dbProgramMap, dbProjectTypeMap, stringListHashMap));
                                createMembers(type, program, project ,stringListHashMap.get("Resource"), dbProjectMembersMap, dbProgramResourceMap);
                            }
                    } else if(type.trim().equals(GROUP)) {
                        if (isFiledsValid(type, i, stringListHashMap)) {
                                programProject = programProjectRepository.save(createOrUpdateProgramProject(i, stringListHashMap.get("Name").trim(), stringListHashMap, dbProgamProjectsMap, program));
                            }
                    } else if(type.trim().equals(PROJECT)) {
                        String typePath = stringListHashMap.get("Type Path");
                        if (isFiledsValid(type, i, stringListHashMap)) {
                                project = projectRepository.save(createProjectByPath(i, stringListHashMap.get("Name").trim(), typePath, dbProjectMap, dbProjectTypeMap, stringListHashMap, program));
                                createMembers(type, program, project ,stringListHashMap.get("Team"), dbProjectMembersMap, dbProgramResourceMap);
                                if(programProject.getId() != null && program.getId() != null) {
                                    PLMProgramProject programProject1 = dbProgamProjectsMap.get("null" + program.getId() + project.getId());
                                    if(programProject1 == null) {
                                        PLMProgramProject programProject2 = new PLMProgramProject();
                                        programProject2.setProject(project.getId());
                                        programProject2.setProgram(program.getId());
                                        programProject2.setType(ProgramProjectType.PROJECT);
                                        programProject2.setParent(programProject.getId());
                                        programProject2 = programProjectRepository.save(programProject2);
                                        dbProgamProjectsMap.put("null" + programProject2.getProgram() + programProject2.getProject(), programProject2);
                                    } else {
                                        programProject1.setProject(project.getId());
                                        programProject1.setProgram(program.getId());
                                        programProject1.setType(ProgramProjectType.PROJECT);
                                        programProject1.setParent(programProject.getId());
                                        programProject1 = programProjectRepository.save(programProject1);
                                    }
                                }
                            }
                    } else if(type.trim().equals(PHASE)) {
                        if(project.getId() != null) {
                            if (isFiledsValid(type, i, stringListHashMap)) {
                                phase = wbsElementRepository.save(createOrUpdatePhase(i, stringListHashMap.get("Name").trim(), stringListHashMap, dbPhaseMap, project));
                            }
                        } else throw new CassiniException("Please provide project details");
                    } else if(type.trim().equals(ACTIVITY)) {
                        if(phase.getId() != null) {
                            if (isFiledsValid(type, i, stringListHashMap)) {
                                activity = activityRepository.save(createOrUpdateActivity(i, stringListHashMap.get("Name").trim(), stringListHashMap, dbProjectTypeMap, dbActivityMap, phase));
                            }
                        } else throw new CassiniException("Please provide phase details");
                    } else if(type.trim().equals(TASK)) {
                        if(activity.getId() != null) {
                            if (isFiledsValid(type, i, stringListHashMap)) {
                                taskRepository.save(createOrUpdateTask(i, stringListHashMap.get("Name").trim(), stringListHashMap, dbProjectTypeMap, dbTaskMap, activity));
                            }
                        } else throw new CassiniException("Please provide activity details");
                    }  else if(type.trim().equals(MILESTONE)) {
                        if(phase.getId() != null) {
                            if (isFiledsValid(type, i, stringListHashMap)) {
                                milestoneRepository.save(createOrUpdateMilestone(i, stringListHashMap.get("Name").trim(), stringListHashMap, dbMilestoneMap, phase));
                            }
                        } else throw new CassiniException("Please provide phase details");
                    } else {
                        throw new CassiniException("Please provide valid type for row :" + (i+1));
                    } 
                } else {
                    throw new CassiniException("Please provide type for row :" + (i+1));
                }
            }
        } else throw new CassiniException("Please provide project details");
    }

    private Boolean isFiledsValid(String type, Integer i, RowData stringListHashMap) {
        Boolean isValid = true;
        if(type == null || type.trim().equals("")) {
            isValid = false;
            throw new CassiniException("Please provide Type for row :" + (i+1));
        } else {
            if(type.trim().equals(PROGRAM)) {
                if(stringListHashMap.get("Name") == null || stringListHashMap.get("Name").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide program name for row :" + (i+1));
                } 
                if(stringListHashMap.get("Resource") == null || stringListHashMap.get("Resource").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide resource members for row :" + (i+1));
                } 
                if(stringListHashMap.get("Program/Project Manager") == null || stringListHashMap.get("Program/Project Manager").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide program/project manager for row :" + (i+1));
                } 
            }
            if(type.trim().equals(PROJECT)) {
                if(stringListHashMap.get("Name") == null || stringListHashMap.get("Name").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide project name for row :" + (i+1));
                } 
                if(stringListHashMap.get("Team") == null || stringListHashMap.get("Team").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide team members for row :" + (i+1));
                } 
                if((stringListHashMap.get("Project Manager") == null || stringListHashMap.get("Project Manager").trim().equals(""))
                    && (stringListHashMap.get("Program/Project Manager") == null || stringListHashMap.get("Program/Project Manager").trim().equals(""))) {
                    isValid = false;
                    throw new CassiniException("Please provide project manager for row :" + (i+1));
                } 
                if(stringListHashMap.get("Planned Start Date") == null || stringListHashMap.get("Planned Start Date").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide planned start date for row :" + (i+1));
                } 
                if(stringListHashMap.get("Duration") == null || stringListHashMap.get("Duration").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide duration for row :" + (i+1));
                } 
            }  else if(type.trim().equals(PHASE)) {
                if(stringListHashMap.get("Name") == null || stringListHashMap.get("Name").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide phase name for row :" + (i+1));
                }  
                if(stringListHashMap.get("Planned Start Date") == null || stringListHashMap.get("Planned Start Date").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide planned start date for row :" + (i+1));
                }  
                if(stringListHashMap.get("Duration") == null || stringListHashMap.get("Duration").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide duration for row :" + (i+1));
                } 
            } else if(type.trim().equals(ACTIVITY) || type.trim().equals(TASK) || type.trim().equals(MILESTONE)) {
                if(stringListHashMap.get("Name") == null || stringListHashMap.get("Name").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide activity name for row :" + (i+1));
                }  
                if(stringListHashMap.get("Planned Start Date") == null || stringListHashMap.get("Planned Start Date").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide planned start date for row :" + (i+1));
                }  
                if(stringListHashMap.get("Duration") == null || stringListHashMap.get("Duration").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide duration for row :" + (i+1));
                }
                if(stringListHashMap.get("Assigned To") == null || stringListHashMap.get("Assigned To").trim().equals("")) {
                    isValid = false;
                    throw new CassiniException("Please provide assigned to for row :" + (i+1));
                }
            } 
        }
        
        return isValid;
    }

     private PLMProgram createProgramByPath(int i, String name, String typePath, Map<String, PLMProgram> dbProgramMap,
                                       Map<String, PMObjectType> dbProjectTypeMap, RowData stringListHashMap) {
        PLMProgram program = null;
        if (typePath != null && typePath != "") {
            PMObjectType pmObjectType = this.getProjectType(typePath);
            if (pmObjectType != null) {
                dbProjectTypeMap.put(pmObjectType.getName().trim(), pmObjectType);
                program = createOrUpdateProgram(i, name, pmObjectType, stringListHashMap, dbProgramMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                        null, "Please provide Program Type or Program Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                    null, "Please provide Program Type or Program Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return program;
    }

    private PLMProject createProjectByPath(int i, String name, String typePath, Map<String, PLMProject> dbProjectMap,
                                       Map<String, PMObjectType> dbProjectTypeMap, RowData stringListHashMap, PLMProgram program) {
        PLMProject project = null;
        PMObjectType pmObjectType = new PMObjectType();
        if (typePath != null && typePath != "") {
            pmObjectType = this.getProjectType(typePath);
            if (pmObjectType == null) pmObjectType = dbProjectTypeMap.get(PROJECT);
        }
        dbProjectTypeMap.put(pmObjectType.getName().trim(), pmObjectType);
        project = dbProjectMap.get(name + program.getId());
        project = createOrUpdateProject(i, name, pmObjectType, stringListHashMap, dbProjectMap, program);
        return project;
    }

    private PLMProgram createOrUpdateProgram(Integer i,String name, PMObjectType pmObjectType, RowData stringListHashMap, Map<String, PLMProgram> dbProgamMap) {
        PLMProgram program1 = dbProgamMap.get(name);
        PLMProgram program = new PLMProgram();
        try {
        if (program1 == null) {
            program = setProgramObject(stringListHashMap, program, pmObjectType);
            dbProgamMap.put(program.getName().trim(), program);
        } else {
            program = setProgramObject(stringListHashMap, program1, pmObjectType);
        }
    } catch (Exception e) {
        throw new CassiniException(e.getMessage());
    }   
        return program;
    }

    private PLMProgramProject createOrUpdateProgramProject(Integer i,String name, RowData stringListHashMap, Map<String, PLMProgramProject> dbProgamProjectsMap, PLMProgram program) {
        PLMProgramProject programProject1 = dbProgamProjectsMap.get(name + program.getId() + null);
        PLMProgramProject programProject = new PLMProgramProject();
        try {
        if (programProject1 == null) {
            programProject = setProgramProjectObject(stringListHashMap, programProject, program);
            dbProgamProjectsMap.put(programProject.getName().trim() + program.getId() + null, programProject);
        } else {
            programProject = setProgramProjectObject(stringListHashMap, programProject1, program);
        }
    } catch (Exception e) {
        throw new CassiniException(e.getMessage());
    }   
        return programProject;
    }

    private PLMProject createOrUpdateProject(Integer i,String name, PMObjectType pmObjectType, RowData stringListHashMap, Map<String, PLMProject> dbProjectMap, PLMProgram program) {
        PLMProject project1 = dbProjectMap.get(name + program.getId());
        PLMProject project = new PLMProject();
        try {
        if (project1 == null) {
            project = setProjectObject(stringListHashMap, project, pmObjectType, program);
            dbProjectMap.put(project.getName().trim() + project.getId(), project);
        } else {
            project = setProjectObject(stringListHashMap, project1, pmObjectType, program);
        }
    } catch (Exception e) {
        throw new CassiniException(e.getMessage());
    }   
        return project;
    }

    private PLMWbsElement createOrUpdatePhase(Integer i, String name, RowData stringListHashMap, Map<String, PLMWbsElement> dbPhaseMap, PLMProject project) {
        PLMWbsElement wbsElement1 = dbPhaseMap.get(name + project.getId());
        PLMWbsElement wbsElement = new PLMWbsElement();
        try {
            if (wbsElement1 == null) {
                wbsElement = setPhaseObject(i, wbsElement, project, stringListHashMap);
                dbPhaseMap.put(wbsElement.getName().trim() + project.getId(), wbsElement);
            } else {
                wbsElement = setPhaseObject(i, wbsElement1, project, stringListHashMap);
            }
        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }   
        return wbsElement;
    }

    private PLMActivity createOrUpdateActivity(Integer i, String name, RowData stringListHashMap, 
                        Map<String, PMObjectType>  dbProjectTypeMap,Map<String, PLMActivity> dbActivityMap, PLMWbsElement wbsElement) {
        PLMActivity activity1 = dbActivityMap.get(name + wbsElement.getId());
        PLMActivity activity = new PLMActivity();
        try {
            if (activity1 == null) {
                activity = setActivityObject(i, activity, wbsElement, stringListHashMap, dbProjectTypeMap);
                dbActivityMap.put(activity.getName().trim() + wbsElement.getId(), activity);
            } else {
                activity = setActivityObject(i, activity1, wbsElement, stringListHashMap, dbProjectTypeMap);
            }

        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }   
        return activity;
    }

   private PLMTask createOrUpdateTask(Integer i, String name,RowData stringListHashMap , Map<String, PMObjectType>  dbProjectTypeMap, Map<String, PLMTask> dbTaskMap, PLMActivity activity) {
        PLMTask task1 = dbTaskMap.get(name + activity.getId());
        PLMTask task = new PLMTask();
        try {
            if (task1 == null) {
                task = setTaskObject(i, task, activity, stringListHashMap, dbProjectTypeMap);
                dbTaskMap.put(task.getName().trim() + activity.getId(), task);
            } else {
                task = setTaskObject(i, task1, activity, stringListHashMap, dbProjectTypeMap);
            }
        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }   
        return task;
    }

    private PLMMilestone createOrUpdateMilestone(Integer i, String name, RowData stringListHashMap , Map<String, PLMMilestone> dbMilestoneMap, PLMWbsElement wbsElement) {
        PLMMilestone milestone1 = dbMilestoneMap.get(name + wbsElement.getId());
        PLMMilestone milestone = new PLMMilestone();
        try {
            if (milestone1 == null) {
                milestone = setMilestoneObject(i, wbsElement, milestone, stringListHashMap);
                dbMilestoneMap.put(milestone.getName().trim() + wbsElement.getId(), milestone);
            } else {
                milestone = setMilestoneObject(i, wbsElement, milestone1,stringListHashMap);
            }

        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }   
        return milestone;
    }

    private Person getPerson(String name, String type) {
        Person person = new Person(); 
        if(name.contains(" ")) {
            String fullName[] = name.split(" ");

            if(personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(fullName[0], fullName[1].trim()) != null) {
                person = personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(fullName[0], fullName[1]);
            } else throw new CassiniException("Please provide a valid " + type);
        } else {
            if(personRepository.getByFirstName(name) != null) {
                person = personRepository.getByFirstName(name);
            } else throw new CassiniException("Please provide a valid " + type);
        }
        return person;
    }

    private PLMProgram setProgramObject(RowData stringListHashMap, PLMProgram program, PMObjectType pmObjectType) {
        try {
            String projectName = stringListHashMap.get("Name").trim();
            String description = stringListHashMap.get("Description");
            String projectManager = stringListHashMap.get("Program/Project Manager").trim();
            program.setName(projectName);
            program.setType(pmObjectType);
            program.setDescription(description);
            Person projectManager1 = getPerson(projectManager, "project manager");
            if(projectManager1 != null) program.setProgramManager(projectManager1.getId());
        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }
        return program;
    }

    private PLMProgramProject setProgramProjectObject(RowData stringListHashMap, PLMProgramProject programProject, PLMProgram program) {
        try {
            String projectName = stringListHashMap.get("Name").trim();
            String description = stringListHashMap.get("Description");
            programProject.setName(projectName);
            programProject.setDescription(description);
            programProject.setProgram(program.getId());
        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }
        return programProject;
    }

    private PLMProject setProjectObject(RowData stringListHashMap, PLMProject project, PMObjectType pmObjectType, PLMProgram program) {
        try {
            String projectName = stringListHashMap.get("Name").trim();
            String description = stringListHashMap.get("Description");
            String plannedStartDate = stringListHashMap.get("Planned Start Date").trim();
            String projectManager = null;
            if(stringListHashMap.get("Project Manager") != null) projectManager = stringListHashMap.get("Project Manager").trim();
            if(stringListHashMap.get("Program/Project Manager") != null && projectManager == null) projectManager = stringListHashMap.get("Program/Project Manager").trim();
            project.setName(projectName);
            project.setType(pmObjectType);
            project.setDescription(description);
            Person projectManager1 = getPerson(projectManager, "project manager");
            if(projectManager1 != null) project.setProjectManager(projectManager1.getId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
            Date plannedStartDate1 = dateFormat.parse(plannedStartDate);
            project.setPlannedStartDate(plannedStartDate1);
            Calendar c = Calendar.getInstance();
            c.setTime(plannedStartDate1);
            c.add(Calendar.DATE, (int) Double.parseDouble(stringListHashMap.get("Duration").trim()));
            project.setPlannedFinishDate(c.getTime());
            if(program != null) project.setProgram(program.getId());
        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }
        return project;
    }

    private PLMWbsElement setPhaseObject(Integer i, PLMWbsElement wbsElement, PLMProject project, RowData stringListHashMap) {
        try {
            String phaseName = stringListHashMap.get("Name").trim();
            String description = stringListHashMap.get("Description");
            String plannedStartDate = stringListHashMap.get("Planned Start Date").trim();
            wbsElement.setName(phaseName);
            wbsElement.setDescription(description);
            wbsElement.setProject(project);
            wbsElement.setSequenceNumber(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
            Date plannedStartDate1 = dateFormat.parse(plannedStartDate);
            wbsElement.setPlannedStartDate(plannedStartDate1);
            Calendar c = Calendar.getInstance();
            c.setTime(plannedStartDate1);
            c.add(Calendar.DATE, (int) Double.parseDouble(stringListHashMap.get("Duration").trim()));
            wbsElement.setPlannedFinishDate(c.getTime());
            if(wbsElement.getPlannedStartDate().before(project.getPlannedStartDate()) || wbsElement.getPlannedStartDate().after(project.getPlannedFinishDate())) {
                throw new  CassiniException("Phase planned start date should in between project planned start and finish dates for row: " + i);
            }
            if(wbsElement.getPlannedFinishDate().before(project.getPlannedStartDate()) || wbsElement.getPlannedFinishDate().after(project.getPlannedFinishDate())) {
                throw new  CassiniException("Phase planned finish date should in between project planned start and finish dates for row: " + i);
            }
        } catch (ParseException e) {
            throw new CassiniException(e.getMessage());
        }
        return wbsElement;   
    }

    private PLMActivity setActivityObject(Integer i, PLMActivity activity, PLMWbsElement wbsElement, RowData stringListHashMap, Map<String, PMObjectType>  dbProjectTypeMap) {
        try {
            PMObjectType pmObjectType = dbProjectTypeMap.get(ACTIVITY);
            String phaseName = stringListHashMap.get("Name").trim();
            String description = stringListHashMap.get("Description");
            String plannedStartDate = stringListHashMap.get("Planned Start Date").trim();
            Integer duration = (int) Double.parseDouble(stringListHashMap.get("Duration").trim());
            activity.setName(phaseName);
            activity.setDescription(description);
            activity.setWbs(wbsElement.getId());
            activity.setType(pmObjectType);
            activity.setSequenceNumber(i);
            activity.setStatus(ProjectActivityStatus.PENDING);
            activity.setDuration(duration);
            String assignedTo = stringListHashMap.get("Assigned To").trim();
            Person assignedTo1 = getPerson(assignedTo, "assigned to");
            if(assignedTo1 != null) activity.setAssignedTo(assignedTo1.getId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
            Date plannedStartDate1 = dateFormat.parse(plannedStartDate);
            activity.setPlannedStartDate(plannedStartDate1);
            Calendar c = Calendar.getInstance();
            c.setTime(plannedStartDate1);
            c.add(Calendar.DATE, duration);
            activity.setPlannedFinishDate(c.getTime());
            if(activity.getPlannedStartDate().before(wbsElement.getPlannedStartDate()) || activity.getPlannedStartDate().after(wbsElement.getPlannedFinishDate())) {
                throw new  CassiniException("Activity planned start date should in between phase planned start and finish dates for row: " + i);
            }
            if(activity.getPlannedFinishDate().before(wbsElement.getPlannedStartDate()) || activity.getPlannedFinishDate().after(wbsElement.getPlannedFinishDate())) {
                throw new  CassiniException("Activity planned finish date should in between phase planned start and finish dates for row: " + i);
            }
        } catch (ParseException e) {
            throw new CassiniException(e.getMessage());
        }
        return activity;   
    }

    private PLMTask setTaskObject(Integer i, PLMTask task, PLMActivity activity, RowData stringListHashMap, Map<String, PMObjectType>  dbProjectTypeMap) {
        try {
            PMObjectType pmObjectType = dbProjectTypeMap.get(TASK);
            String phaseName = stringListHashMap.get("Name").trim();
            String description = stringListHashMap.get("Description");
            String plannedStartDate = stringListHashMap.get("Planned Start Date").trim();
            Integer duration = (int) Double.parseDouble(stringListHashMap.get("Duration").trim());
            task.setName(phaseName);
            task.setDescription(description);
            task.setActivity(activity.getId());
            task.setType(pmObjectType);
            task.setDuration(duration);
            task.setSequenceNumber(i);
            String assignedTo = stringListHashMap.get("Assigned To").trim();
            Person assignedTo1 = getPerson(assignedTo, "assigned to");
            if(assignedTo1 != null) task.setAssignedTo(assignedTo1.getId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
            Date plannedStartDate1 = dateFormat.parse(plannedStartDate);
            task.setPlannedStartDate(plannedStartDate1);
            Calendar c = Calendar.getInstance();
            c.setTime(plannedStartDate1);
            c.add(Calendar.DATE, duration);
            task.setPlannedFinishDate(c.getTime());
            if(task.getPlannedStartDate().before(activity.getPlannedStartDate()) || task.getPlannedStartDate().after(activity.getPlannedFinishDate())) {
                throw new  CassiniException("Task planned start date should in between activity planned start and finish dates for row: " + i);
            }
            if(task.getPlannedFinishDate().before(activity.getPlannedStartDate()) || task.getPlannedFinishDate().after(activity.getPlannedFinishDate())) {
                throw new  CassiniException("Task planned finish date should in between activity planned start and finish dates for row: " + i);
            }
        } catch (ParseException e) {
            throw new CassiniException(e.getMessage());
        }
        return task;   
    }

    private PLMMilestone setMilestoneObject(Integer i, PLMWbsElement wbsElement, PLMMilestone milestone, RowData stringListHashMap) {
        try {
            String phaseName = stringListHashMap.get("Name").trim();
            String description = stringListHashMap.get("Description");
            String plannedFinishDate = stringListHashMap.get("Planned Start Date").trim();
            milestone.setName(phaseName);
            milestone.setDescription(description);
            milestone.setWbs(wbsElement.getId());
            milestone.setSequenceNumber(i);
            milestone.setStatus(ProjectActivityStatus.PENDING);
            String assignedTo = stringListHashMap.get("Assigned To").trim();
            Person assignedTo1 = getPerson(assignedTo, "assigned to");
            if(assignedTo1 != null) milestone.setAssignedTo(assignedTo1.getId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
            Date plannedFinishDate1 = dateFormat.parse(plannedFinishDate);
            milestone.setPlannedFinishDate(plannedFinishDate1);
            if(milestone.getPlannedFinishDate().before(wbsElement.getPlannedStartDate()) || milestone.getPlannedFinishDate().after(wbsElement.getPlannedFinishDate())) {
                throw new  CassiniException("Milestone planned finish date should in between phase planned start and finish dates for row: " + i);
            }
        } catch (ParseException e) {
            throw new CassiniException(e.getMessage());
        }
        return milestone;   
    }
    
}
