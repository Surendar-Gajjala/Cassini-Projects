package com.cassinisys.plm.model.analytics.quality;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 17-07-2020.
 */
@Data
public class QualityByTypeDto {
    private List<Integer> qualityTypes = new LinkedList<>();

    private List<Integer> inspectionPlansByStatus = new LinkedList<>();

    private List<Integer> inspectionsByStatus = new LinkedList<>();

    private List<Integer> prsByStatus = new LinkedList<>();

    private List<Integer> prsBySource = new LinkedList<>();

    private List<Integer> ncrsByStatus = new LinkedList<>();

    private List<Integer> qcrsByStatus = new LinkedList<>();

    private List<Integer> qcrsByType = new LinkedList<>();

    private List<Integer> ppapByStatus = new LinkedList<>();

    private List<Integer> supplierAuditsByStatusCounts = new LinkedList<>();


}
