package com.cassinisys.platform.model.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PMListDto {
    List<Object> programList = new ArrayList<>();
    List<Object> programResourceList = new ArrayList<>();
    List<Object> projectList = new ArrayList<>();
    List<Object> projectMembersList = new ArrayList<>();
    List<Object> projectTaskList = new ArrayList<>();
    List<Object> projectActivityList = new ArrayList<>();
    List<Object> fileList = new ArrayList<>();
}
