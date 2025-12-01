package com.cassinisys.platform.model.common;

import lombok.Data;

import java.util.List;

/**
 * Created by lakshmi on 08-10-2017.
 */
@Data
public class Export {

    private String fileName;

    private List<String> headers;

    private List<ExportRow> exportRows;


}
