package com.cassinisys.is.model.procm.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Nageshreddy on 26-09-2018.
 */
public class MaterialTypeStoresReportDTO implements Serializable {

    private List<String> headers;
    private Map<String, List<StoresReportDTO>> isMaterialTypeListMap;

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public Map<String, List<StoresReportDTO>> getIsMaterialTypeListMap() {
        return isMaterialTypeListMap;
    }

    public void setIsMaterialTypeListMap(Map<String, List<StoresReportDTO>> isMaterialTypeListMap) {
        this.isMaterialTypeListMap = isMaterialTypeListMap;
    }
}
