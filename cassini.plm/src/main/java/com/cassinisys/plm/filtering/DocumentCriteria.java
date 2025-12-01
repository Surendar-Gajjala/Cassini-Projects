package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Suresh Cassini on 18-11-2020.
 */
public class DocumentCriteria extends Criteria {
    Integer objectId;
    Integer objectFolder;
    Integer folder;
    String searchQuery;
    String documentType;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getObjectFolder() {
        return objectFolder;
    }

    public void setObjectFolder(Integer objectFolder) {
        this.objectFolder = objectFolder;
    }

    public Integer getFolder() {
        return folder;
    }

    public void setFolder(Integer folder) {
        this.folder = folder;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}