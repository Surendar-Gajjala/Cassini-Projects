package com.cassinisys.platform.service.core;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ObjectTypeService {
    private Map<String, TypeSystem> mapTypeSystems = new HashMap<>();

    public void registerTypeSystem(String objectType, TypeSystem typeSystem) {
        mapTypeSystems.put(objectType, typeSystem);
    }

    public TypeSystem getTypeSystem(String objectType) {
        return mapTypeSystems.get(objectType);
    }

}
