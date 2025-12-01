package com.cassinisys.platform.service.core;

        import java.util.List;

public interface SharedTypeSystem {
    Object get(Integer id);

    List getSharedToList(Integer personId);
}
