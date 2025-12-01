package com.cassinisys.platform.model.dto;


import lombok.Data;

import java.util.List;

@Data
public class ClassDto {

    public String className;
    public List<PropertiesDto> properties;
}
