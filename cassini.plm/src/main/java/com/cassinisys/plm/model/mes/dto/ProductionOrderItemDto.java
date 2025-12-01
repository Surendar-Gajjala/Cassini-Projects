package com.cassinisys.plm.model.mes.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smukka on 22-08-2022.
 */
@Data
public class ProductionOrderItemDto {
    private Integer id;
    private Integer productionOrder;
    private Integer mbomRevision;
    private Integer bopRevision;
    private Integer quantityProduced;
    private String name;
    private String number;
    private String revision;
    private String description;
    private String objectType;
    private List<MBOMInstanceDto> children = new ArrayList<>();
}
