package com.cassinisys.platform.filtering;

import java.util.LinkedList;
import java.util.List;

/**
 * @author reddy
 */
public class ActivityStreamCriteria extends Criteria {

    private Integer objectId;
    private Integer user;
    private String date;
    private String type;
    private String action;
    private List<Integer> objectIds = new LinkedList<>();

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getObjectIds() {
        return objectIds;
    }

    public void setObjectIds(List<Integer> objectIds) {
        this.objectIds = objectIds;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
