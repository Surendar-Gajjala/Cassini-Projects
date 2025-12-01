package com.cassinisys.plm.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 20-03-2020.
 */
@Data
public class BomModalDto {

    private Integer id;

    private ItemDto item;

    private RevisionDto parent;

    private Integer sequenceNumber;

    private List<ItemAttributeDto> attributes = new ArrayList<>();

    private List<BomModalDto> children = new ArrayList<>();

}
