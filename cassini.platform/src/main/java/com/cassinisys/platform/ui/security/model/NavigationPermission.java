package com.cassinisys.platform.ui.security.model;

import java.util.List;

/**
 * Created by lakshmi on 7/10/2016.
 */

public class NavigationPermission {
    private String navigation;
    private List<String> permissions;

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
