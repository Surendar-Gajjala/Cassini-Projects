package com.cassinisys.plm.model.analytics.projects;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by CassiniSystems on 19-07-2020.
 */
@Data
public class ProjectTypeDto {

    private List<Integer> projectByStatus = new LinkedList<>();

    private List<Integer> activityByStatus = new LinkedList<>();

    private List<Integer> taskByStatus = new LinkedList<>();

    private List<Integer> reqByStatus = new LinkedList<>();

    private List<Integer> projectDeliverableByStatus = new LinkedList<>();

    private List<Integer> activityDeliverableByStatus = new LinkedList<>();

    private List<Integer> taskDeliverableByStatus = new LinkedList<>();

    private List<String> openProjects = new LinkedList<>();

    private List<Long> openTasks = new LinkedList<>();

    private List<String> reqByTypes = new LinkedList<>();

    private List<Long> reqCounts = new LinkedList<>();

    private ActivityStatusCount activityStatusCount;

    private List<String> projects = new LinkedList<>();

    private Integer totalActivityStatusCounts;

    private Integer totalProjectsCounts;

    private ProgramProjectStatusCount programProjectStatusCount;

}


