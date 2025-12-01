package com.cassinisys.plm.model.dto;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.dto.BomDataDto;
import com.cassinisys.plm.model.plm.dto.BomDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 10-05-2020.
 */
@Data
public class BomWhereUsedReport {

    private List<BomDto> bomItems = new LinkedList<>();

    private List<PLMItem> items = new ArrayList<>();

    private BomDataDto bomDataDto;

}
