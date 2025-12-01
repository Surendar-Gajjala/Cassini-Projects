package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.core.ObjectType;

/**
 * Created by swapna on 04/02/19.
 */
public class MessageGroupCriteria extends Criteria {

    private String name;

    private String description;

    private ObjectType ctxObjectType;

    private Integer ctxObjectId;

    private String searchQuery = "";

    private boolean freeTextSearch = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectType getCtxObjectType() {
        return ctxObjectType;
    }

    public void setCtxObjectType(ObjectType ctxObjectType) {
        this.ctxObjectType = ctxObjectType;
    }

    public Integer getCtxObjectId() {
        return ctxObjectId;
    }

    public void setCtxObjectId(Integer ctxObjectId) {
        this.ctxObjectId = ctxObjectId;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public boolean isFreeTextSearch() {
        return freeTextSearch;
    }

    public void setFreeTextSearch(boolean freeTextSearch) {
        this.freeTextSearch = freeTextSearch;
    }
}
