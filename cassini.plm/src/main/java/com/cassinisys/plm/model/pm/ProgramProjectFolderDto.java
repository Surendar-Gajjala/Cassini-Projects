package com.cassinisys.plm.model.pm;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by smukka on 30-08-2022.
 */
@Data
public class ProgramProjectFolderDto {
    private Integer id;
    private Integer program;
    private Integer project;
    private Integer parent;
    private String name;
    private String objectType;
    private List<ProgramProjectFolderDto> children = new LinkedList<>();
}
