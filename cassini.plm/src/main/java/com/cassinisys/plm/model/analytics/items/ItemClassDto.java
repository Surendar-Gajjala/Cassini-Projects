package com.cassinisys.plm.model.analytics.items;

import lombok.Data;

/**
 * Created by subramanyam on 21-07-2020.
 */
@Data
public class ItemClassDto {
    private Integer products = 0;
    private Integer assemblies = 0;
    private Integer parts = 0;
    private Integer documents = 0;
    private Integer others = 0;


}
