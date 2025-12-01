package com.cassinisys.plm.model.analytics.changes;

import lombok.Data;

/**
 * Created by subramanyam on 19-07-2020.
 */
@Data
public class ChangeCardCounts {
    private Integer ecrs = 0;
    private Integer ecos = 0;
    private Integer dcrs = 0;
    private Integer dcos = 0;
    private Integer mcos = 0;
    private Integer deviations = 0;
    private Integer waivers = 0;


}
