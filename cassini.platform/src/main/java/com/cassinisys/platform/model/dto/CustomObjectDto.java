package com.cassinisys.platform.model.dto;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectAttribute;
import com.cassinisys.platform.model.custom.CustomObjectType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 11-10-2020.
 */
@Data
public class CustomObjectDto {

    private List<CustomObjectAttribute> customObjectAttributes = new ArrayList<>();

    private List<ObjectAttribute> objectAttributes = new ArrayList<>();

    private CustomObject customObject;
    private Integer customObjectTypeId;
    private CustomObjectType customObjectType;

}
