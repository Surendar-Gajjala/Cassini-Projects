package com.cassinisys.platform.service.core;


import com.cassinisys.platform.model.dto.PMListDto;

public interface ProgramTypeSystem {
    Object get(Integer id);

    PMListDto getPMToList(Integer personId);
    
}
