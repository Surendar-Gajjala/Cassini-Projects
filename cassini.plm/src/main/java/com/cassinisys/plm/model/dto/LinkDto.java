package com.cassinisys.plm.model.dto;

import lombok.Data;

/**
 * Created by subramanyam on 20-03-2020.
 */
@Data
public class LinkDto {
    private Long id;
    private String type;
    private Integer source;
    private Integer target;
}
