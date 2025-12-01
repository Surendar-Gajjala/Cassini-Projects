package com.cassinisys.erp.ui.security;

import com.cassinisys.erp.service.security.SessionWrapper;
import com.cassinisys.erp.ui.security.model.NavigationItem;
import com.cassinisys.erp.ui.security.model.NavigationPermission;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by reddy on 9/11/15.
 */
@Component
public class SecureNavigationBuilder {
    @Autowired
    private SessionWrapper sessionWrapper;

    @Value("classpath:navigation.json")
    private Resource navigationResource;

    @Value("classpath:navigationPermissions.json")
    private Resource navigationPermissionsResource;

    private List<NavigationItem> navigationItems = new ArrayList<>();
    private Map<String, NavigationPermission> navigationPermissions = new HashMap<>();



    public SecureNavigationBuilder() {

    }

    private void init() {
        try {
            navigationItems = new ObjectMapper().readValue(navigationResource.getInputStream(),
                    new TypeReference<List<NavigationItem>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error loading navigation file", e);
        }

        try {
            List<NavigationPermission> list = new ObjectMapper().readValue(navigationPermissionsResource.getInputStream(),
                    new TypeReference<List<NavigationPermission>>() {});
            for(NavigationPermission permission : list) {
                navigationPermissions.put(permission.getNavigation(), permission);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading navigation file", e);
        }
    }

    public List<NavigationItem> build() {
        init();

        if(sessionWrapper == null || sessionWrapper.getSession() == null) {
            throw new RuntimeException("You have to be logged in to access this information");
        }
        else if (!sessionWrapper.getAuthorization().isAdministrator()) {
            navigationItems = filterNavigation(navigationItems);
        }

        return navigationItems;
    }

    private List<NavigationItem> filterNavigation(List<NavigationItem> allNavItems) {
        List<NavigationItem> filteredNavItems = new ArrayList<>();

        for(NavigationItem navItem : allNavItems) {
            String id = navItem.getId();
            NavigationPermission navPermission = navigationPermissions.get(id);

            if(navPermission != null) {
                List<String> permissions = navPermission.getPermissions();
                if(permissions.size() == 0) {
                    filteredNavItems.add(navItem);
                }
                else {
                    if(sessionWrapper.getAuthorization().hasOneOfPermissions(permissions)) {
                        filteredNavItems.add(navItem);
                    }
                }
            }

            List<NavigationItem> children = navItem.getChildren();
            navItem.setChildren(filterNavigation(children));
        }

        return filteredNavItems;
    }

}
