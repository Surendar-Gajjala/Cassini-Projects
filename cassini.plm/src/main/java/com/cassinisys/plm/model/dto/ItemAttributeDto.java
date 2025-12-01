package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.plm.model.plm.ItemConfigurableAttributes;
import lombok.Data;

/**
 * Created by subramanyam on 20-03-2020.
 */
@Data
public class ItemAttributeDto {

    private ObjectAttributeId id;

    private ItemConfigurableAttributes itemAttribute;

    private String listValue;

}
