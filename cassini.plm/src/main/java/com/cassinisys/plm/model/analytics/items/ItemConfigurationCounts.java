package com.cassinisys.plm.model.analytics.items;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 20-07-2020.
 */
@Data
public class ItemConfigurationCounts {

    private List<Integer> itemsByConfigurations = new LinkedList<>();

}
