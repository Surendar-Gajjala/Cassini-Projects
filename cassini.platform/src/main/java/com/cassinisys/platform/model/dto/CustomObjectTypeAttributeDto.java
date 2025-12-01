package com.cassinisys.platform.model.dto;

import com.cassinisys.platform.model.custom.CustomObjectAttribute;
import com.cassinisys.platform.model.custom.CustomObjectTypeAttribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 15-10-2020.
 */
@Data
public class CustomObjectTypeAttributeDto {

    private List<CustomObjectTypeAttribute> customObjectTypeAttributes = new ArrayList<>();
    private List<CustomObjectAttribute> customObjectAttributes = new ArrayList<>();
}
