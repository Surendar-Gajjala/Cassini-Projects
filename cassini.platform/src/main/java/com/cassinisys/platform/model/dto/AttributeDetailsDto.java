package com.cassinisys.platform.model.dto;

import com.cassinisys.platform.model.core.DataType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smukka on 01-11-2022.
 */
@Data
public class AttributeDetailsDto {
    private Integer id;
    private String attributeName;
    private Boolean listMultiple = false;
    private DataType dataType;
    private String stringValue;
    private Integer integerValue;
    private Double doubleValue;
    private String dateValue;
    private String booleanValue;
    private String listValue;
    private List<String> multiValues = new ArrayList<>();
    private Double currencyValue;
    private String formulaValue;
    private List<String> attachments = new ArrayList<>();
    private String longTextValue;
    private String hyperLinkValue;
    private String richTextValue;
    private String timeValue;
    private String timestampValue;
    private String formula;
}
