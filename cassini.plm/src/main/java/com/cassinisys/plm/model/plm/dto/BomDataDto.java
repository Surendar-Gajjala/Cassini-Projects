package com.cassinisys.plm.model.plm.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by subramanyam on 24-05-2020.
 */
@Data
public class BomDataDto {

    private String name;

    private Integer itemId;

    private List<BomDataDto> children;


}
