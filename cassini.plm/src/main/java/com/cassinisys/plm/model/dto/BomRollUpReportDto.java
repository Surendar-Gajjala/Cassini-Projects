package com.cassinisys.plm.model.dto;

import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 06-05-2020.
 */
@Data
public class BomRollUpReportDto {

    private List<PLMItemTypeAttribute> bomAttributes = new LinkedList<>();

    private List<BomRollUpReport> bomItems = new LinkedList<>();

}
