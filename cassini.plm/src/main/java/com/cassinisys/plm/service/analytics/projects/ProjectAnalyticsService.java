package com.cassinisys.plm.service.analytics.projects;

import com.cassinisys.plm.model.analytics.projects.*;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.req.PLMRequirementRepository;
import com.cassinisys.plm.repo.req.PLMRequirementTypeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementVersionRepository;
import com.cassinisys.plm.service.pm.ProgramService;
import com.cassinisys.plm.service.pm.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by GSR on 19-07-2020.
 */
@Service
public class ProjectAnalyticsService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PLMActivityRepository plmActivityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectDeliveravbleRepository projectDeliveravbleRepository;
    @Autowired
    private ActivityDeliverableRepository activityDeliverableRepository;
    @Autowired
    private TaskDeliverableRepository taskDeliverableRepository;
    @Autowired
    private PLMRequirementRepository requirementRepository;
    @Autowired
    private PLMRequirementVersionRepository requirementVersionRepository;
    @Autowired
    private PLMRequirementTypeRepository requirementTypeRepository;
    @Autowired
    private WbsElementRepository wbsElementRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProgramProjectRepository programProjectRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProgramService programService;


    @Transactional(readOnly = true)
    public ProjectTypeDto getProjectDashboardCounts() {
        ProjectTypeDto typeDto = new ProjectTypeDto();
        List<PLMProject> projects = projectRepository.findByProgramIsNull();
        ActivityStatusCount activityStatusCount = new ActivityStatusCount();

        typeDto.getProjectByStatus().add(projectRepository.getNotYetStartedProjects());
        typeDto.getProjectByStatus().add(projectRepository.getInProgressProjects());
        typeDto.getProjectByStatus().add(projectRepository.getFinishedProjects());
        typeDto.getProjectByStatus().add(projectRepository.getOverdueProjects());

        typeDto.getActivityByStatus().add(plmActivityRepository.getByStatusTypeActivity(ProjectActivityStatus.PENDING));
        typeDto.getActivityByStatus().add(plmActivityRepository.getByStatusTypeActivity(ProjectActivityStatus.INPROGRESS));
        typeDto.getActivityByStatus().add(plmActivityRepository.getFinishedActivity(ProjectActivityStatus.FINISHED));
        typeDto.getActivityByStatus().add(plmActivityRepository.getOverdueActivity());

        typeDto.getTaskByStatus().add(taskRepository.getByStatusTypeTask(ProjectTaskStatus.PENDING));
        typeDto.getTaskByStatus().add(taskRepository.getByStatusTypeTask(ProjectTaskStatus.INPROGRESS));
        typeDto.getTaskByStatus().add(taskRepository.getFinishedTask(ProjectTaskStatus.FINISHED));
        typeDto.getTaskByStatus().add(taskRepository.getOverdueTask());

        typeDto.getProjectDeliverableByStatus().add(projectDeliveravbleRepository.getByProjectDeliverableByStatus(DeliverableStatus.PENDING));
        typeDto.getProjectDeliverableByStatus().add(projectDeliveravbleRepository.getByProjectDeliverableByStatus(DeliverableStatus.FINISHED));

        typeDto.getActivityDeliverableByStatus().add(activityDeliverableRepository.getByActivityDeliverableByStatus(DeliverableStatus.PENDING));
        typeDto.getActivityDeliverableByStatus().add(activityDeliverableRepository.getByActivityDeliverableByStatus(DeliverableStatus.FINISHED));

        typeDto.getTaskDeliverableByStatus().add(taskDeliverableRepository.getByTaskDeliverableByStatus(DeliverableStatus.PENDING));
        typeDto.getTaskDeliverableByStatus().add(taskDeliverableRepository.getByTaskDeliverableByStatus(DeliverableStatus.FINISHED));

        //typeDto.getReqByStatus().add(requirementRepository.getRequirementsByStatus(LifeCyclePhaseType.PRELIMINARY));
        //typeDto.getReqByStatus().add(requirementRepository.getRequirementsByStatus(LifeCyclePhaseType.REVIEW));
        //typeDto.getReqByStatus().add(requirementRepository.getRequirementsByStatus(LifeCyclePhaseType.RELEASED));

        typeDto.setReqByTypes(requirementRepository.getRequirementsTypes());
        typeDto.setReqCounts(requirementRepository.getRequirementsCounts());

        typeDto.setOpenProjects(projectRepository.getProjectUnfinishedProjects(ProjectTaskStatus.FINISHED));
        typeDto.setOpenTasks(projectRepository.getProjectUnfinishedTaskCount(ProjectTaskStatus.FINISHED));

        typeDto.setTotalProjectsCounts(projects.size());
        typeDto.setTotalActivityStatusCounts(plmActivityRepository.getActivityCountByProgramProgramIsNull());

        projects.forEach(project -> {
            typeDto.getProjects().add(project.getName());
            Integer pendingCount = 0;
            Integer inProgressCount = 0;
            Integer finishedCount = 0;
            Integer overDueCount = 0;
            List<PLMWbsElement> wbsElements = wbsElementRepository.findByProject(project);
            for (PLMWbsElement wbs : wbsElements) {
                pendingCount = pendingCount + plmActivityRepository.getByStatusTypeActivityAndProject(wbs.getId(), ProjectActivityStatus.PENDING);
                inProgressCount = inProgressCount + plmActivityRepository.getByStatusTypeActivityAndProject(wbs.getId(), ProjectActivityStatus.INPROGRESS);
                finishedCount = finishedCount + plmActivityRepository.getFinishedActivityAndProject(wbs.getId(), ProjectActivityStatus.FINISHED);
                overDueCount = overDueCount + plmActivityRepository.getOverdueActivityAndProject(wbs.getId());
            }
            activityStatusCount.getPendingCounts().add(pendingCount);
            activityStatusCount.getInProgressCounts().add(inProgressCount);
            activityStatusCount.getFinishedCounts().add(finishedCount);
            activityStatusCount.getOverDueCounts().add(overDueCount);
        });
        typeDto.setActivityStatusCount(activityStatusCount);
        return typeDto;
    }


    @Transactional(readOnly = true)
    public ProgramProjectStatusCount getProgramProjectStatusCount() {
        ProgramProjectStatusCount programProjectStatusCount = new ProgramProjectStatusCount();

        List<PLMProgram> programs = programRepository.findAllByOrderByIdAsc();

        Map<String, Map<String, Integer>> programProjectMap = new LinkedHashMap<>();
        programProjectMap.put("Not Started", new LinkedHashMap<>());
        programProjectMap.put("In Progress", new LinkedHashMap<>());
        programProjectMap.put("Finished", new LinkedHashMap<>());
        programs.forEach(program -> {
            programProjectStatusCount.getPrograms().add(program.getName());
            Map<String, Integer> notStartedMap = programProjectMap.get("Not Started");
            Map<String, Integer> inProgressMap = programProjectMap.get("In Progress");
            Map<String, Integer> finishedMap = programProjectMap.get("Finished");
            List<Integer> projectIds = programProjectRepository.getProgramProjectIds(program.getId());
            notStartedMap.put(program.getName(), 0);
            inProgressMap.put(program.getName(), 0);
            finishedMap.put(program.getName(), 0);
            if (projectIds.size() > 0) {
                projectIds.forEach(project -> {
                    Double percent = projectService.getProjectPercentComplete(project).getPercentComplete();
                    if (percent == 0.0) {
                        Integer count = notStartedMap.containsKey(program.getName()) ? notStartedMap.get(program.getName()) : 0;
                        count += 1;
                        notStartedMap.put(program.getName(), count);
                        programProjectMap.put("Not Started", notStartedMap);
                    } else if (percent == 100) {
                        Integer count = finishedMap.containsKey(program.getName()) ? finishedMap.get(program.getName()) : 0;
                        count += 1;
                        finishedMap.put(program.getName(), count);
                        programProjectMap.put("Finished", finishedMap);
                    } else {
                        Integer count = inProgressMap.containsKey(program.getName()) ? inProgressMap.get(program.getName()) : 0;
                        count += 1;
                        inProgressMap.put(program.getName(), count);
                        programProjectMap.put("In Progress", inProgressMap);
                    }
                });
            }
            programProjectMap.put("Not Started", notStartedMap);
            programProjectMap.put("In Progress", inProgressMap);
            programProjectMap.put("Finished", finishedMap);
        });

        for (String key : programProjectMap.keySet()) {
            Map<String, Integer> monthMap = programProjectMap.get(key);
            ProjectStatusCount projectStatusCount = new ProjectStatusCount();
            projectStatusCount.setName(key);
            projectStatusCount.setData(new LinkedList<>(monthMap.values()));
            programProjectStatusCount.getProjectStatusCounts().add(projectStatusCount);
        }

        return programProjectStatusCount;
    }


    /*
    * Constructing drilldown chart series data
    * */

    public Object[] generateChartCoordinates(String x, int y, String dataGroupId) {
        Object[] randArray = new Object[3];
        randArray[0] = x;
        randArray[1] = y;
        randArray[2] = dataGroupId;
        return randArray;
    }

    /*
    * Generating root level  data
    * */

    public DrillDownDTO getLevelData() {
        DrillDownDTO data = new DrillDownDTO();
        List<PLMProgram> programs = programRepository.findAllByOrderByIdAsc();
        List<Object> list = new LinkedList<>();
        DrillDownMap mapObject = new DrillDownMap();
        Map<String, List<ProgramProjectDto>> programsMap = new ConcurrentHashMap<>();
        //Level - 1
        mapObject.setDataGroupId("1");
        mapObject.setColorByLevel("#008FFB");
        mapObject.setLegend("Program");
        int i = 0;
        for (PLMProgram program : programs) {
            String key = String.valueOf(i);
            List<ProgramProjectDto> grpCounts = programService.getProgramProjects(program.getId());
            programsMap.put(program.getId() + "-" + program.getName(), grpCounts);
            Object[] randArray = generateChartCoordinates(program.getName(), grpCounts.size(), program.getId() + "-" + program.getName());
            list.add(randArray);
            i++;

        }
        mapObject.setData(list);
        data.setPpMap(programsMap);
        data.setDrillDownMap(mapObject);

        return data;
    }



    /*
    * Generating  level-1  data
    * */

    public DrillDownDTO getLevelOneData(Map<String, List<ProgramProjectDto>> programsMap) {
        DrillDownDTO data = new DrillDownDTO();
        List<DrillDownMap> listData = new LinkedList<>();
        List<Object> list = new LinkedList<>();
        DrillDownMap mapObject = new DrillDownMap();
        Map<String, List<ProgramProjectDto>> grpsMap = new ConcurrentHashMap<>();
        Iterator<String> it1 = programsMap.keySet().iterator();
        while (it1.hasNext()) {
            mapObject = new DrillDownMap();
            list = new LinkedList<>();
            String key = it1.next();
            mapObject.setDataGroupId(key);
            mapObject.setColorByLevel("#2ECC71");
            mapObject.setLegend("Group");
            List<ProgramProjectDto> value = programsMap.get(key);
            // checks for null value
            if (value != null) {
                // iterates over String elements of value
                for (ProgramProjectDto element : value) {
                    // checks for null
                    if (element != null) {
                        grpsMap.put(element.getId() + "-" + element.getName(), element.getChildren());
                        Object[] randArray = generateChartCoordinates(element.getName(), element.getChildren().size(), element.getId() + "-" + element.getName());
                        list.add(randArray);

                    }
                }
                mapObject.setData(list);
                listData.add(mapObject);

            }
        }


        data.setPpMap(grpsMap);
        data.setListData(listData);

        return data;
    }

    /*
    * Generating  level-2  data
    * */

    public DrillDownDTO getLevelTwoData(Map<String, List<ProgramProjectDto>> grpsMap) {
        List<PLMProject> projects = projectRepository.findAll();
        Map<Integer, PLMProject> elementsMap = new ConcurrentHashMap();
        elementsMap = projects.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        DrillDownDTO data = new DrillDownDTO();
        List<DrillDownMap> listData = new LinkedList<>();
        List<Object> list = new LinkedList<>();
        DrillDownMap mapObject = new DrillDownMap();
        Map<String, List<WBSCountDto>> projectsMap = new ConcurrentHashMap<>();
        Iterator<String> it2 = grpsMap.keySet().iterator();

        while (it2.hasNext()) {
            mapObject = new DrillDownMap();
            list = new LinkedList<>();
            String key = it2.next();
            mapObject.setDataGroupId(key);
            mapObject.setColorByLevel("#DC7633");
            mapObject.setLegend("Project");
            List<ProgramProjectDto> value = grpsMap.get(key);
            // checks for null value
            if (value != null) {
                // iterates over String elements of value
                for (ProgramProjectDto element : value) {
                    // checks for null
                    if (element != null) {
                        if (element.getProject() != null) {
                            PLMProject project = elementsMap.get(element.getProject());
                            List<WBSCountDto> phases = projectService.getDrillDownProjectPlanStructure(project);
                            projectsMap.put(project.getId() + "-" + project.getName(), phases);
                            Object[] randArray = generateChartCoordinates(project.getName(), phases.size(), project.getId() + "-" + project.getName());
                            list.add(randArray);
                        }
                    }
                }
                mapObject.setData(list);
                listData.add(mapObject);
            }
        }


        data.setProjectsMap(projectsMap);
        data.setListData(listData);

        return data;
    }


    /*
    * Generating  level-3  data
    * */

    public DrillDownDTO getLevelThreeData(Map<String, List<WBSCountDto>> projectsMap) {
        DrillDownDTO data = new DrillDownDTO();
        List<DrillDownMap> listData = new LinkedList<>();
        List<Object> list = new LinkedList<>();
        DrillDownMap mapObject = new DrillDownMap();
        Map<String, List<WBSCountDto>> phasesMap = new ConcurrentHashMap<>();
        Iterator<String> it3 = projectsMap.keySet().iterator();


        while (it3.hasNext()) {
            mapObject = new DrillDownMap();
            list = new LinkedList<>();
            String key = it3.next();
            mapObject.setDataGroupId(key);
            mapObject.setColorByLevel("#E74C3C");
            mapObject.setLegend("Phase");
            List<WBSCountDto> value = projectsMap.get(key);
            // checks for null value
            if (value != null) {
                // iterates over String elements of value
                for (WBSCountDto element : value) {
                    // checks for null
                    if (element != null) {
                        phasesMap.put(element.getId() + "-" + element.getName(), element.getChildren());
                        Object[] randArray = generateChartCoordinates(element.getName(), element.getChildren().size(), element.getId() + "-" + element.getName());
                        list.add(randArray);


                    }
                }
                mapObject.setData(list);
                listData.add(mapObject);
            }
        }


        data.setProjectsMap(phasesMap);
        data.setListData(listData);

        return data;
    }



     /*
    * Generating  level-4  data
    * */

    public DrillDownDTO getLevelFourData(Map<String, List<WBSCountDto>> phasesMap) {
        DrillDownDTO data = new DrillDownDTO();
        List<DrillDownMap> listData = new LinkedList<>();
        List<Object> list = new LinkedList<>();
        DrillDownMap mapObject = new DrillDownMap();
        Map<String, List<PLMTask>> activityMap = new ConcurrentHashMap<>();
        Iterator<String> it4 = phasesMap.keySet().iterator();
        while (it4.hasNext()) {
            mapObject = new DrillDownMap();
            mapObject.setFinalLevel("Final");
            list = new LinkedList<>();
            String key = it4.next();
            mapObject.setDataGroupId(key);
            mapObject.setColorByLevel("#F4D03F");
            mapObject.setLegend("Activity");
            // gets the value
            List<WBSCountDto> value = phasesMap.get(key);
            // checks for null value
            if (value != null) {
                // iterates over String elements of value
                for (WBSCountDto element : value) {
                    // checks for null
                    if (element != null) {
                        activityMap.put(element.getId() + "-" + element.getName(), element.getActivityTasks());
                        Object[] randArray = generateChartCoordinates(element.getName(), element.getActivityTasks().size(), element.getId() + "-" + element.getName());
                        list.add(randArray);
                    }
                }
                mapObject.setData(list);
                listData.add(mapObject);
            }
        }


        data.setActivityMap(activityMap);
        data.setListData(listData);

        return data;
    }

      /*
    * Generating  level-5  data
    * */

    public DrillDownDTO getLevelFiveData(Map<String, List<PLMTask>> activityMap) {
        DrillDownDTO data = new DrillDownDTO();
        List<DrillDownMap> listData = new LinkedList<>();
        List<Object> list = new LinkedList<>();
        DrillDownMap mapObject = new DrillDownMap();
        Map<String, List<PLMTask>> tasksMap = new ConcurrentHashMap<>();
        Iterator<String> it5 = activityMap.keySet().iterator();


        while (it5.hasNext()) {
            int pending = 0;
            int inprogress = 0;
            int finished = 0;
            List<PLMTask> pendingList = new LinkedList<>();
            List<PLMTask> inProgressList = new LinkedList<>();
            List<PLMTask> finishedList = new LinkedList<>();
            mapObject = new DrillDownMap();
            list = new LinkedList<>();
            String key = it5.next();
            mapObject.setDataGroupId(key);
            mapObject.setColorByLevel("#148F77");
            mapObject.setLegend("Task");
            mapObject.setConfirmedPath(true);
            // gets the value
            List<PLMTask> value = activityMap.get(key);
            // checks for null value
            if (value != null) {
                // iterates over String elements of value
                for (PLMTask element : value) {
                    // checks for null
                    if (element != null) {
                        if (element.getStatus().equals(ProjectTaskStatus.PENDING)) {
                            pendingList.add(element);
                            pending++;
                        }

                        if (element.getStatus().equals(ProjectTaskStatus.INPROGRESS)) {
                            inProgressList.add(element);
                            inprogress++;
                        }

                        if (element.getStatus().equals(ProjectTaskStatus.FINISHED)) {
                            finishedList.add(element);
                            finished++;
                        }


                    }
                }

                if (pending >= 0) {
                    tasksMap.put(key + "-" + "Pending", pendingList);
                    Object[] randArray = generateChartCoordinates("Pending", pending, key + "-" + "Pending");
                    list.add(randArray);
                }
                if (inprogress >= 0) {
                    tasksMap.put(key + "-" + "Inprogress", inProgressList);
                    Object[] randArray = generateChartCoordinates("Inprogress", inprogress, key + "-" + "Inprogress");
                    list.add(randArray);
                }
                if (finished >= 0) {
                    tasksMap.put(key + "-" + "Finished", finishedList);
                    Object[] randArray = generateChartCoordinates("Finished", finished, key + "-" + "Finished");
                    list.add(randArray);
                }
                mapObject.setData(list);
                listData.add(mapObject);
            }
        }


        data.setActivityMap(tasksMap);
        data.setListData(listData);

        return data;
    }

     /*
    * Generating  level-5  data
    * */

    public DrillDownDTO getLevelSixData(Map<String, List<PLMTask>> tasksMap) {
        DrillDownDTO data = new DrillDownDTO();
        List<DrillDownMap> listData = new LinkedList<>();
        List<Object> list = new LinkedList<>();
        DrillDownMap mapObject = new DrillDownMap();
        Iterator<String> it6 = tasksMap.keySet().iterator();


        while (it6.hasNext()) {
            mapObject = new DrillDownMap();
            list = new LinkedList<>();
            String key = it6.next();
            mapObject.setDataGroupId(key);
            mapObject.setStage(true);
            List<PLMTask> value = tasksMap.get(key);
            // checks for null value
            if (value != null) {
                // iterates over String elements of value
                for (PLMTask element : value) {
                    // checks for null
                    if (element != null) {
                        int percentageComplete = 0;
                        if (element.getPercentComplete() != null) {
                            percentageComplete = element.getPercentComplete().intValue();
                        }
                        Object[] randArray = generateChartCoordinates(element.getName(), percentageComplete, element.getName());
                        list.add(randArray);
                    }
                }
                mapObject.setData(list);
                listData.add(mapObject);
            }
        }


        // data.setActivityMap(tasksMap);
        data.setListData(listData);

        return data;
    }

    @Transactional(readOnly = true)
    public List<DrillDownMap> getProgramDrillDownReport() {
        List<DrillDownMap> finalData = new LinkedList<>();

        //Root level
        DrillDownDTO rootLevelData = getLevelData();
        finalData.add(rootLevelData.getDrillDownMap());

        //Level - 1
        DrillDownDTO levelOneData = getLevelOneData(rootLevelData.getPpMap());

        finalData.addAll(levelOneData.getListData());

        //Level - 2
        DrillDownDTO levelTwoData = getLevelTwoData(levelOneData.getPpMap());

        finalData.addAll(levelTwoData.getListData());

        //Level - 3
        DrillDownDTO levelThreeData = getLevelThreeData(levelTwoData.getProjectsMap());

        finalData.addAll(levelThreeData.getListData());


        //Level - 4
        DrillDownDTO levelFourData = getLevelFourData(levelThreeData.getProjectsMap());

        finalData.addAll(levelFourData.getListData());


        //Level - 5
        DrillDownDTO levelFiveData = getLevelFiveData(levelFourData.getActivityMap());

        finalData.addAll(levelFiveData.getListData());


        //Level - 6
        DrillDownDTO levelSixData = getLevelSixData(levelFiveData.getActivityMap());

        finalData.addAll(levelSixData.getListData());


        return finalData;

    }

}
