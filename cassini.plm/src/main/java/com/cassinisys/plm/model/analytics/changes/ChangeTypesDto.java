package com.cassinisys.plm.model.analytics.changes;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 17-07-2020.
 */
@Data
public class ChangeTypesDto {
    private List<Integer> changeTypes = new LinkedList<>();

    private List<Integer> ecrCounts = new LinkedList<>();

    private List<Integer> ecoCounts = new LinkedList<>();

    private List<Integer> dcrCounts = new LinkedList<>();

    private List<Integer> dcoCounts = new LinkedList<>();

    private List<Integer> mcoCounts = new LinkedList<>();

    private List<Integer> deviationCounts = new LinkedList<>();

    private List<Integer> waiverCounts = new LinkedList<>();


}
