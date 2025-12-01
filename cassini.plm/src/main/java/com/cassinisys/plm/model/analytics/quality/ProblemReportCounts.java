package com.cassinisys.plm.model.analytics.quality;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 17-07-2020.
 */
@Data
public class ProblemReportCounts {

    List<String> prSeverities = new LinkedList<>();
    List<String> ncrSeverities = new LinkedList<>();

    List<Integer> prSeverityCounts = new LinkedList<>();
    List<Integer> ncrSeverityCounts = new LinkedList<>();

    List<String> prFailures = new LinkedList<>();
    List<String> ncrFailures = new LinkedList<>();

    List<Integer> prFailureCounts = new LinkedList<>();
    List<Integer> ncrFailureCounts = new LinkedList<>();

    List<String> prDispositions = new LinkedList<>();
    List<String> ncrDispositions = new LinkedList<>();

    List<Integer> prDispositionCounts = new LinkedList<>();
    List<Integer> ncrDispositionCounts = new LinkedList<>();

}
