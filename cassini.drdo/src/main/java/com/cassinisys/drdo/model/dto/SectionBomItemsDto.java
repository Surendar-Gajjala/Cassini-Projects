package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 05-03-2019.
 */
public class SectionBomItemsDto {

    private BomItem section;

    private List<BomItem> bomItems = new ArrayList<>();

    public BomItem getSection() {
        return section;
    }

    public void setSection(BomItem section) {
        this.section = section;
    }

    public List<BomItem> getBomItems() {
        return bomItems;
    }

    public void setBomItems(List<BomItem> bomItems) {
        this.bomItems = bomItems;
    }
}
