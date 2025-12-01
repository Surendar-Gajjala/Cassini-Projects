package com.cassinisys.platform.service.core;

import java.util.List;

public interface TypeSystem {
    Boolean isSubtypeOf(String compareWith, Object typeName, Integer subTypeId);

    Object get(Integer id);

    List getTypeAttributes(Integer typeId, Boolean hierarchy);
}
