package com.cassinisys.erp.ui.security.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 9/11/15.
 */
public class NavigationItem {
    private String id;
    private String text;
    private String icon;
    private String state;
    private boolean active = false;
    private List<NavigationItem> children = new ArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<NavigationItem> getChildren() {
        return children;
    }

    public void setChildren(List<NavigationItem> children) {
        this.children = children;
    }
}
