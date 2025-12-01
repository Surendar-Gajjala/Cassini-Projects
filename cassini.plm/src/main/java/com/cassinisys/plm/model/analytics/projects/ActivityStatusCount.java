package com.cassinisys.plm.model.analytics.projects;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;
@Data
public class ActivityStatusCount {
    private List<Integer> pendingCounts = new LinkedList<>();
    private List<Integer> inProgressCounts = new LinkedList<>();
    private List<Integer> finishedCounts = new LinkedList<>();
    private List<Integer> overDueCounts = new LinkedList<>();
}