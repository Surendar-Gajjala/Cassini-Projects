package com.cassinisys.drdo.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subramanyam reddy on 09-10-2018.
 */
public class ItemSearchCriteria extends Criteria {

    private String searchQuery;

    private Integer item;

    private Integer selectedItem;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Integer selectedItem) {
        this.selectedItem = selectedItem;
    }
}
