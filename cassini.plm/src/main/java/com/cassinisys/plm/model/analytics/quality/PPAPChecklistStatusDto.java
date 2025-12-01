package com.cassinisys.plm.model.analytics.quality;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class PPAPChecklistStatusDto {
    private List<String> ppapFolders = new LinkedList<>();

    private List<PPAPFolderReportDto> foldersReports = new LinkedList<>();
    
}
