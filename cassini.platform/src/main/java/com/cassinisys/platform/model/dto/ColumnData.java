package com.cassinisys.platform.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ColumnData implements Serializable {
    private List<String> names = new ArrayList<>();
}
