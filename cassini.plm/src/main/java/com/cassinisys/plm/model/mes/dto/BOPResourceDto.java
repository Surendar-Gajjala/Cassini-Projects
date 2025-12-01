package com.cassinisys.plm.model.mes.dto;

import lombok.Data;

/**
 * Created by smukka on 05-08-2022.
 */
@Data
public class BOPResourceDto {
    private String resource;
    private Integer quantity = 0;
    private Integer consumedQty = 0;
}
