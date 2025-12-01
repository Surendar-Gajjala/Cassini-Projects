package com.cassinisys.is.filtering;
/**
 * The class is for ItemInventoryCriteria
 */

import com.cassinisys.platform.filtering.Criteria;

public class ItemInventoryCriteria extends Criteria {

    Integer boqItem;
    Integer store;
    String stockOnHand;
    String stockOnOrder;
    private boolean freeTextSearch = false;
    private String searchQuery;

    /**
     * The methods getters and setters are used to get and set values of different classes and data types
     */
    public Integer getBoqItem() {
        return boqItem;
    }

    public void setBoqItem(Integer boqItem) {
        this.boqItem = boqItem;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getStockOnHand() {
        return stockOnHand;
    }

    public void setStockOnHand(String stockOnHand) {
        this.stockOnHand = stockOnHand;
    }

    public String getStockOnOrder() {
        return stockOnOrder;
    }

    public void setStockOnOrder(String stockOnOrder) {
        this.stockOnOrder = stockOnOrder;
    }

    public boolean isFreeTextSearch() {
        return freeTextSearch;
    }

    public void setFreeTextSearch(boolean freeTextSearch) {
        this.freeTextSearch = freeTextSearch;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
