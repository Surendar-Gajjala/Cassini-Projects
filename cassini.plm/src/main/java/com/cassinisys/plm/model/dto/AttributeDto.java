package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import lombok.Data;

/**
 * Created by subramanyam on 20-03-2020.
 */
@Data
public class AttributeDto {

    private ObjectAttributeId id;

    private PLMItemTypeAttribute attribute;

    private String listValue;


}
