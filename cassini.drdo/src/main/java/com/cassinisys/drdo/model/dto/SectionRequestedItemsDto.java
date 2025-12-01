package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomGroup;
import com.cassinisys.drdo.model.bom.BomInstanceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 19-02-2019.
 */
public class SectionRequestedItemsDto {

    private BomGroup section;

    private List<BomInstanceItem> bomInstanceItems = new ArrayList<>();

    public BomGroup getSection() {
        return section;
    }

    public void setSection(BomGroup section) {
        this.section = section;
    }

    public List<BomInstanceItem> getBomInstanceItems() {
        return bomInstanceItems;
    }

    public void setBomInstanceItems(List<BomInstanceItem> bomInstanceItems) {
        this.bomInstanceItems = bomInstanceItems;
    }
}
