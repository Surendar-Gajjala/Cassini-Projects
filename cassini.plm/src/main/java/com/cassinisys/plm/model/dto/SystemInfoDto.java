package com.cassinisys.plm.model.dto;

import com.cassinisys.plm.model.plm.PLMItemType;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CassiniSystems on 05-12-2019.
 */
@Data
public class SystemInfoDto {

    private Integer items = 0;
    private Integer files = 0;
    private String fileSystemSize;
    private List<PLMItemType> itemTypes = new ArrayList<>();
    private Map<String, Integer> listHashMap = new LinkedHashMap<>();
    Map<String, Map<String, Integer>> adminInfo = new LinkedHashMap<String, Map<String, Integer>>();

}
