package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 07-12-2018.
 */
public class StoragePartsDto {

    private List<BomItem> uniqueParts = new ArrayList<>();

    private List<BomItem> commonParts = new ArrayList<>();

    public List<BomItem> getUniqueParts() {
        return uniqueParts;
    }

    public void setUniqueParts(List<BomItem> uniqueParts) {
        this.uniqueParts = uniqueParts;
    }

    public List<BomItem> getCommonParts() {
        return commonParts;
    }

    public void setCommonParts(List<BomItem> commonParts) {
        this.commonParts = commonParts;
    }
}
