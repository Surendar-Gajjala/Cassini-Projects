package com.cassinisys.test.model;

import lombok.Data;

import java.util.List;
@Data
public class RunCaseDetailsDTO {
    private RunCase runCase;
    private List<RunInputParam> runInputParams;
    private List<RunOutPutParam> runOutPutParams;

}