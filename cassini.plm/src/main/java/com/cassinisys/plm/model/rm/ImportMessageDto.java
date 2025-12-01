package com.cassinisys.plm.model.rm;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 05-10-2018.
 */
@Data
public class ImportMessageDto {

    private List<Integer> ignoredRows = new ArrayList<>();

    private Integer executedRows = 0;

    private String ignoredRowsMessage = null;

}
