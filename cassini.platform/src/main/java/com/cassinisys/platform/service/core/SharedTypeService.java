package com.cassinisys.platform.service.core;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SharedTypeService {
    private Map<String, SharedTypeSystem> mapTypeSystems = new HashMap<>();

    public void registerTypeSystem(String objectType, SharedTypeSystem typeSystem) {
        mapTypeSystems.put(objectType, typeSystem);
    }

    public SharedTypeSystem getTypeSystem(String objectType) {
        return mapTypeSystems.get(objectType);
    }

}
