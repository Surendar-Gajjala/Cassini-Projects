package com.cassinisys.plm.model.dto;

import com.cassinisys.plm.model.plm.PLMBom;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
@Data
public class BomItemComparisionDTO {
    private String fromItemName;
    private String toItemName;
    private String fromItemNumber;
    private String toItemNumber;
    private String fromItemRev;
    private String toItemRev;

    private List<PLMBom> itemList = new LinkedList<>();

}