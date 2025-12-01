package com.cassinisys.plm.model.dto;

import com.cassinisys.plm.model.plm.dto.BomDto;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 06-05-2020.
 */
@Data
public class BomRollUpReport {

    private BomDto bomItem;

    private List<BomRollUpAttribute> bomRollUpAttributes = new LinkedList<>();

    private List<BomRollUpReport> children = new LinkedList<>();

}
