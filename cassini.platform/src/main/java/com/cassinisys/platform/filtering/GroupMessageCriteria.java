package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.core.ObjectType;

/**
 * Created by swapna on 08/02/19.
 */
public class GroupMessageCriteria extends Criteria {

    private String msgText;
    private ObjectType ctxObjectType;
    private Integer ctxObjectId;
    private Integer msgGrpId;
    private String searchQuery = "";
    private boolean freeTextSearch = false;

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
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

    public Integer getMsgGrpId() {
        return msgGrpId;
    }

    public void setMsgGrpId(Integer msgGrpId) {
        this.msgGrpId = msgGrpId;
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
