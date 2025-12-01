package com.cassinisys.plm.model.analytics.quality;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class PPAPFolderReportDto {

    private String name;

    private List<Integer> data = new LinkedList<>();
    
}
