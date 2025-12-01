package com.cassinisys.platform.service.core;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemRevisionTypeService {
    private Map<String, ItemRevisionTypeSystem> mapTypeSystems = new HashMap<>();

    public void registerTypeSystem(String objectType, ItemRevisionTypeSystem typeSystem) {
        mapTypeSystems.put(objectType, typeSystem);
    }

    public ItemRevisionTypeSystem getTypeSystem(String objectType) {
        return mapTypeSystems.get(objectType);
    }

}
