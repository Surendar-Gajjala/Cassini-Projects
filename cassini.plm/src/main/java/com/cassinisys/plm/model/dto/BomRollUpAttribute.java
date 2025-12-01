package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectAttribute;
import lombok.Data;

/**
 * Created by subramanyam on 06-05-2020.
 */
@Data
public class BomRollUpAttribute {

//    private PLMItemTypeAttribute itemTypeAttribute;

    private String name;
    private DataType dataType;
    private Integer measurementUnit;
    private Integer measurement;

    private ObjectAttribute objectAttribute;

    private Double rollUpValue = 0.0;

    private Double multipliedValue = 0.0;

    private String unitSymbol;

    private Boolean unitValue = Boolean.TRUE;

    private Boolean approximated = Boolean.FALSE;

    private Double actualValue = 0.0;

    private Double actualMultipliedValue = 0.0;

}
