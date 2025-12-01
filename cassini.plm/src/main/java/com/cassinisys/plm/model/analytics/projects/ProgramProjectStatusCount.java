package com.cassinisys.plm.model.analytics.projects;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by smukka on 15-06-2022.
 */
@Data
public class ProgramProjectStatusCount {
    private List<String> programs = new LinkedList<>();

    private List<ProjectStatusCount> projectStatusCounts = new LinkedList<>();
}
