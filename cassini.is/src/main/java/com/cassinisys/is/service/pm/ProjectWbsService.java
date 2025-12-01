package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.pm.GanttLinkDTO;
import com.cassinisys.is.model.pm.ISLinks;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.pm.ISProjectWbs;
import com.cassinisys.is.model.tm.ISTask;
import com.cassinisys.is.model.tm.ISTaskCompletionHistory;
import com.cassinisys.is.model.tm.TaskStatus;
import com.cassinisys.is.repo.pm.LinksRepository;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.pm.ProjectWbsRepository;
import com.cassinisys.is.repo.tm.ISTaskCompletionHistoryRepository;
import com.cassinisys.is.repo.tm.TaskRepository;
import com.cassinisys.is.service.tm.ProjectTaskService;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.text.SimpleDateFormat.*;

/**
 * The class is for ProjectWbsService
 */
@Service
@Transactional
public class ProjectWbsService {

    @Autowired
    private ProjectWbsRepository projectWbsRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ISTaskCompletionHistoryRepository taskCompletionHistoryRepository;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private LinksRepository linksRepository;

    /**
     * The method used to create ISProjectWbs
     **/
    @Transactional(readOnly = false)
    public ISProjectWbs create(ISProjectWbs projectWbs) {
        checkNotNull(projectWbs);
        projectWbs.setId(null);
        ISProject project = projectRepository.findOne(projectWbs.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        if (projectWbs.getParent() != null) {
            ISProjectWbs wbs = getProjectWbsName(projectWbs.getProject(), projectWbs.getName(), projectWbs.getParent());
            if (wbs == null) {
                projectWbs = projectWbsRepository.save(projectWbs);
            } else {
                throw new RuntimeException("WBS " + projectWbs.getName() + " already exists");
            }
        } else {
            ISProjectWbs wbs = projectWbsRepository.findByNameAndProjectAndParentIsNull(projectWbs.getName(), projectWbs.getProject());
            if (wbs != null && projectWbs.getName().equals(wbs.getName())) {
                throw new RuntimeException("WBS " + projectWbs.getName() + " already exists");
            } else {
                projectWbs = projectWbsRepository.save(projectWbs);
            }
        }
        return projectWbs;
    }

    /**
     * The method used to update ISProjectWbs
     **/
    @Transactional(readOnly = false)
    public ISProjectWbs update(ISProjectWbs projectWbs) {
        checkNotNull(projectWbs);
        checkNotNull(projectWbs.getId());
        ISProject project = projectRepository.findOne(projectWbs.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        projectWbs = projectWbsRepository.save(projectWbs);
        return projectWbs;
    }

    /**
     * The method used to delete projectWbs ISProjectWbs
     **/
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISProjectWbs projectWbs = projectWbsRepository.findOne(id);
        if (projectWbs == null) {
            throw new ResourceNotFoundException();
        }
        if (projectWbs != null) {
            projectWbsRepository.delete(projectWbs);
        }

    }

    /**
     * The method used to get ISProjectWbs
     **/
    @Transactional(readOnly = true)
    public ISProjectWbs get(Integer id) {
        checkNotNull(id);
        ISProjectWbs projectWbs = projectWbsRepository.findOne(id);
        if (projectWbs == null) {
            throw new ResourceNotFoundException();
        }
        return projectWbs;
    }

    @Transactional(readOnly = true)
    public List<ISTask> getAllTasksByWbsItem(Integer wbsItem) {
        checkNotNull(wbsItem);
        List<ISTask> tasks = taskRepository.findByWbsItemOrderByActualStartDateAsc(wbsItem);
        if (tasks == null) {
            throw new ResourceNotFoundException();
        }
        return tasks;
    }

    /**
     * The method used to getAll for the list of ISProjectWbs
     **/
    @Transactional(readOnly = true)
    public List<ISProjectWbs> getAll() {
        return projectWbsRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISProjectWbs
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectWbs> findAll(Pageable pageable) {
        checkNotNull(pageable);
        return projectWbsRepository.findAll(pageable);
    }

    /**
     * The method used to getChildren for the list of ISProjectWbs
     **/
    @Transactional(readOnly = true)
    public List<ISProjectWbs> getChildren(Integer wbsId) {
        checkNotNull(wbsId);
        return projectWbsRepository.findByParentOrderByCreatedDateAsc(wbsId);
    }

    /**
     * The method used to findMultiple for the list of ISProjectWbs
     **/
    @Transactional(readOnly = true)
    public List<ISProjectWbs> findMultiple(List<Integer> ids) {
        return projectWbsRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public ISProjectWbs getProjectWbsName(Integer projectId, String wbsName, Integer parentId) {
        ISProjectWbs projectWbs = projectWbsRepository.findByProjectAndNameEqualsIgnoreCaseAndParent(projectId, wbsName, parentId);
        return projectWbs;
    }

    @Transactional(readOnly = true)
    public ISProjectWbs getParentWbsByName(Integer projectId, String parentName) {
        List<ISProjectWbs> projectWbs = projectWbsRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(projectId);
        for (ISProjectWbs isProjectWbs : projectWbs) {
            if (isProjectWbs.getName().equalsIgnoreCase(parentName)) {
                return isProjectWbs;
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<ISProjectWbs> getAllWbs(Integer projectId) {
        return projectWbsRepository.findByProjectOrderByIdAsc(projectId);
    }

    @Transactional(readOnly = false)
    public List<ISProjectWbs> finishWbs(Integer wbs) {
        ISProjectWbs projectWbs = projectWbsRepository.findOne(wbs);
        List<ISProjectWbs> wbsList = new ArrayList<>();
        List<ISTask> isTasks = taskRepository.findByWbsItemOrderByActualStartDateAsc(projectWbs.getId());
        if (isTasks.size() > 0) {
            for (ISTask isTask : isTasks) {
                if (isTask.getWorkflow() == null && !isTask.getStatus().equals(TaskStatus.FINISHED)) {
                    if (isTask.getPerson() != null) {
                        List<ISTaskCompletionHistory> isTaskCompletionHistory1 = taskCompletionHistoryRepository.findByTask(isTask.getId());
                        Double totalUnits = 0.0;
                        for (ISTaskCompletionHistory isTaskCompletionHistory : isTaskCompletionHistory1) {
                            totalUnits += isTaskCompletionHistory.getUnitsCompleted();
                        }
                        ISTaskCompletionHistory taskCompletionHistory = new ISTaskCompletionHistory();
                        taskCompletionHistory.setTask(isTask.getId());
                        taskCompletionHistory.setCompletedBy(isTask.getPerson());
                        taskCompletionHistory.setTimeStamp(new Date());
                        taskCompletionHistory.setUnitsCompleted(isTask.getTotalUnits() - totalUnits);
                        taskCompletionHistory = taskCompletionHistoryRepository.save(taskCompletionHistory);
                        isTask.setStatus(TaskStatus.FINISHED);
                        isTask.setActualFinishDate(new Date());
                        if (isTask.getActualStartDate() == null) {
                            isTask.setActualStartDate(new Date());
                        }
                        isTask.setPercentComplete(100.00);
                        projectTaskService.updateWorkOrderStatus(isTask);
                        taskRepository.save(isTask);
                        wbsList = projectService.calculatePercentCompleteParent(isTask.getWbsItem());

                    }
                }
            }

        }
        return wbsList;

    }

    public List<GanttLinkDTO> createGanttChart(Integer projectId, String gantt) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap obj = mapper.readValue(gantt, LinkedHashMap.class);
        ArrayList data = (ArrayList) obj.get("data");
        List<GanttLinkDTO> ganttLinkDTOs = new ArrayList<>();
        data.forEach(val -> {
            ISProjectWbs projectWbs = new ISProjectWbs();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            LinkedHashMap map = (LinkedHashMap) val;

            //Updates taskes
            if (map.get("cassiniId") != null) {
                projectWbs.setId((Integer) map.get("id"));
                projectWbs.setName((String) map.get("name"));
                projectWbs.setProject((Integer) map.get("project"));
                try {
                    projectWbs.setPlannedStartDate(format.parse((String) map.get("plannedStartDate")));
                    projectWbs.setPlannedFinishDate(format.parse((String) map.get("plannedFinishDate")));
                    if (map.get("actualStartDate") != null)
                        projectWbs.setActualStartDate(format.parse((String) map.get("actualStartDate")));
                    if (map.get("actualFinishDate") != null)
                        projectWbs.setActualStartDate(format.parse((String) map.get("actualFinishDate")));
                } catch (ParseException e) {}
                projectWbs.setParent((Integer) map.get("parent"));
                projectWbs.setDuration((Integer) map.get("duration"));
                projectWbs.setAssignedTo((Integer) map.get("assignedTo"));
                projectWbs.setWeightage(0.0);
               projectWbsRepository.save(projectWbs);
            }


            if (map.get("type") != null && map.get("cassiniId") == null) {

                //Creates new tasks with no project
                if (map.get("type").equals("task") && map.get("parnet") == null) {
                    projectWbs.setName((String) map.get("name"));
                    try {
                        projectWbs.setPlannedStartDate(format.parse((String) map.get("plannedStartDate")));
                        projectWbs.setPlannedFinishDate(format.parse((String) map.get("plannedFinishDate")));
                    } catch (ParseException e) {
                    }
                    projectWbs.setProject((Integer) map.get("project"));
                    projectWbs.setDuration((Integer) map.get("duration"));
                    projectWbs.setAssignedTo((Integer) map.get("assignedTo"));
                    projectWbs.setWeightage(0.0);
                    ISProjectWbs projectWbs1 = projectWbsRepository.save(projectWbs);
                    GanttLinkDTO ganttLinkDTO = new GanttLinkDTO();
                    ganttLinkDTO.setCassiniId(projectWbs1.getId());
                    ganttLinkDTO.setGanttId((Long) map.get("id"));
                    ganttLinkDTOs.add(ganttLinkDTO);
                }

                // Create project which has no parent task
                if (map.get("type").equals("project") && map.get("parent") == null) {
                    projectWbs.setName((String) map.get("name"));
                    try {
                        projectWbs.setPlannedStartDate(format.parse((String) map.get("plannedStartDate")));
                        projectWbs.setPlannedFinishDate(format.parse((String) map.get("plannedFinishDate")));
                    } catch (ParseException e) {
                    }
                    projectWbs.setProject((Integer) map.get("project"));
                    projectWbs.setDuration((Integer) map.get("duration"));
                    projectWbs.setAssignedTo((Integer) map.get("assignedTo"));
                    projectWbs.setWeightage(0.0);
                    ISProjectWbs projectWbs1 = projectWbsRepository.save(projectWbs);
                    GanttLinkDTO ganttLinkDTO = new GanttLinkDTO();
                    ganttLinkDTO.setCassiniId(projectWbs1.getId());
                    ganttLinkDTO.setGanttId((Long) map.get("id"));
                    ganttLinkDTOs.add(ganttLinkDTO);
                }

                //Creates project which has parent task
                if (map.get("type").equals("project") && map.get("parent") != null) {
                    String id = map.get("parent").toString();
                    if (id.length() > 10) {
                        ganttLinkDTOs.forEach(val1 -> {
                            if (val1.getGanttId().toString().equals(id)) projectWbs.setParent(val1.getCassiniId());
                        });
                    } else {projectWbs.setParent((Integer) map.get("parent"));}
                    projectWbs.setName((String) map.get("name"));
                    try {
                        projectWbs.setPlannedStartDate(format.parse((String) map.get("plannedStartDate")));
                        projectWbs.setPlannedFinishDate(format.parse((String) map.get("plannedFinishDate")));
                    } catch (ParseException e) {
                    }
                    projectWbs.setProject((Integer) map.get("project"));
                    projectWbs.setDuration((Integer) map.get("duration"));
                    projectWbs.setAssignedTo((Integer) map.get("assignedTo"));
                    projectWbs.setWeightage(0.0);
                    ISProjectWbs projectWbs1 = projectWbsRepository.save(projectWbs);
                    GanttLinkDTO ganttLinkDTO = new GanttLinkDTO();
                    ganttLinkDTO.setCassiniId(projectWbs1.getId());
                    ganttLinkDTO.setGanttId((Long) map.get("id"));
                    ganttLinkDTOs.add(ganttLinkDTO);
                }
            }

            // Creates task for parent task
            if (map.get("parent") != null && map.get("cassiniId") == null && map.get("type") == null) {
                String id = map.get("parent").toString();
                if (id.length() > 10) {
                    ganttLinkDTOs.forEach(val1 -> {
                        if (val1.getGanttId().toString().equals(id)) projectWbs.setParent(val1.getCassiniId());
                    });
                } else {projectWbs.setParent((Integer) map.get("parent"));}
                projectWbs.setName((String) map.get("name"));
                try {
                    projectWbs.setPlannedStartDate(format.parse((String) map.get("plannedStartDate")));
                    projectWbs.setPlannedFinishDate(format.parse((String) map.get("plannedFinishDate")));
                } catch (ParseException e) {
                }
                projectWbs.setProject((Integer) map.get("project"));
                projectWbs.setDuration((Integer) map.get("duration"));
                projectWbs.setAssignedTo((Integer) map.get("assignedTo"));
                projectWbs.setWeightage(0.0);
                ISProjectWbs projectWbs1 = projectWbsRepository.save(projectWbs);
                GanttLinkDTO ganttLinkDTO = new GanttLinkDTO();
                ganttLinkDTO.setCassiniId(projectWbs1.getId());
                ganttLinkDTO.setGanttId((Long) map.get("id"));
                ganttLinkDTOs.add(ganttLinkDTO);
            }
        });
        return ganttLinkDTOs;
    }

    public void createLinks(Integer projectId, String links) {
        ISLinks isLinks = linksRepository.findByProject(projectId);
        if (isLinks != null) {
            isLinks.setId(isLinks.getId());
            isLinks.setProject(projectId);
            isLinks.setDependency(links);
            linksRepository.save(isLinks);
        } else {
            ISLinks links1 = new ISLinks();
            links1.setProject(projectId);
            links1.setDependency(links);
            linksRepository.save(links1);
        }
    }

    public ISLinks getLinks(Integer projectId) {
        return linksRepository.findByProject(projectId);
    }
}
