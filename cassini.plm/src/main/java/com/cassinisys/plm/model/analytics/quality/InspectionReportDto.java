package com.cassinisys.plm.model.analytics.quality;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by smukka on 14-06-2022.
 */
@Data
public class InspectionReportDto {
    private List<String> months = new LinkedList<>();

    private List<MonthInspectionReportDto> monthReports = new LinkedList<>();
}
