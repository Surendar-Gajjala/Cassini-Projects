package com.cassinisys.platform.service.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ProgramTypeService {
    private Map<String, ProgramTypeSystem> mapTypeSystems = new HashMap<>();

    public void registerTypeSystem(String objectType, ProgramTypeSystem typeSystem) {
        mapTypeSystems.put(objectType, typeSystem);
    }

    public ProgramTypeSystem getTypeSystem(String objectType) {
        return mapTypeSystems.get(objectType);
    }
}
