package com.cassinisys.platform.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TableData implements Serializable {
    private ColumnData columns = new ColumnData();
    private List<RowData> rows = new ArrayList<>();

}
