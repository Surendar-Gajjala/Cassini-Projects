package com.cassinisys.plm.model.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ItemToItemCompareDTO {
    private String fromItem;
    private String toItem;
    private String oldValue;
    private String newValue;
    private String propertyName;
    private Boolean changesMade = Boolean.FALSE;
    List<ItemToItemCompareDTO> listOfItemsCompared = new LinkedList<>();

}