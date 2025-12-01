package com.cassinisys.platform.model.dto;


import lombok.Data;

import java.util.List;

@Data
public class EnumDto {

    public String objectType;
    public List<String> values;
}
