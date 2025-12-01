package com.cassinisys.platform.model.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * Created by Rajabrahmachary on 17-10-2018.
 */
@Data
public class AttributesDetailsDTO {


    List<ObjectTypeAttribute> objectTypeAttributes;

    Map<Integer, ObjectAttribute> attributeMap;


}
