package com.cassinisys.plm.model.analytics.items;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 17-07-2020.
 */
@Data
public class ItemDashboardCountsDto {
    List<Integer> itemsByClass = new LinkedList<>();

    List<Integer> itemsByConfigurations = new LinkedList<>();

    List<Integer> productItemsByStatus = new LinkedList<>();

    List<Integer> assemblyItemsByStatus = new LinkedList<>();
    List<Integer> partItemsByStatus = new LinkedList<>();
    List<Integer> documentItemsByStatus = new LinkedList<>();
    List<Integer> otherItemsByStatus = new LinkedList<>();

}
