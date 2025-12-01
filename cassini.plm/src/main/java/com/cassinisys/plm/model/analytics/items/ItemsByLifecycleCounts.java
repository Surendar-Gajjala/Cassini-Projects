package com.cassinisys.plm.model.analytics.items;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 21-07-2020.
 */
@Data
public class ItemsByLifecycleCounts {

    private List<String> productLifeCycles = new LinkedList<>();

    private List<Integer> productLifeCycleCounts = new LinkedList<>();

    private List<String> assemblyLifeCycles = new LinkedList<>();

    private List<Integer> assemblyLifeCycleCounts = new LinkedList<>();

    private List<String> partLifeCycles = new LinkedList<>();

    private List<Integer> partLifeCycleCounts = new LinkedList<>();

    private List<String> documentLifeCycles = new LinkedList<>();

    private List<Integer> documentLifeCycleCounts = new LinkedList<>();

    private List<String> otherLifeCycles = new LinkedList<>();

    private List<Integer> otherLifeCycleCounts = new LinkedList<>();


}
