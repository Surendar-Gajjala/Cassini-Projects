package com.cassinisys.erp.model.production;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lakshmi on 3/26/2016.
 */
public class ProductionItemsBomDTO {

    private Map<Integer,List<ERPBomItem>> bomItems = new HashMap<Integer,List<ERPBomItem>>();

    public Map<Integer, List<ERPBomItem>> getBomItems() {
        return bomItems;
    }

    public void setBomItems(Map<Integer, List<ERPBomItem>> bomItems) {
        this.bomItems = bomItems;
    }
}
