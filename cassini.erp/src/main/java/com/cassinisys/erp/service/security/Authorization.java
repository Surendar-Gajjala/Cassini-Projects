package com.cassinisys.erp.service.security;

import com.cassinisys.erp.model.security.ERPPermission;
import com.cassinisys.erp.model.security.ERPRole;
import com.cassinisys.erp.model.security.ERPSession;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by reddy on 9/11/15.
 */
public final class Authorization implements Serializable {
    private ERPSession session;
    public static final String ADMIN_PERMISSION = "permission.all";

    public Map<String, ERPRole> roles = new HashMap<>();
    public Map<String, ERPPermission> permissions = new HashMap<>();

    public Authorization(ERPSession session) {
        this.session = session;
        init();
    }

    private void init() {
        List<ERPRole> rList = session.getLogin().getRoles();
        for(ERPRole role : rList) {
            roles.put(role.getName(), role);

            List<ERPPermission> pList = role.getPermissions();
            for(ERPPermission permission : pList) {
                permissions.put(permission.getId(), permission);
            }
        }
    }

    public boolean isAdministrator() {
        return hasPermission(ADMIN_PERMISSION);
    }


    public boolean hasRole(String roleName) {
        return roles.get(roleName) != null;
    }

    public boolean hasPermission(String permissionId) {
        boolean hasPermission = false;
        if (permissionId.endsWith(".*")) {
            String prefix = permissionId.substring(0, permissionId.length()-1);
            Set<String> keys = permissions.keySet();
            for(String key : keys) {
                if(key.startsWith(prefix)) {
                    hasPermission = true;
                    break;
                }
            }
        }
        else { //walk up the hierarchy and check
            hasPermission = permissions.get(permissionId) != null;

            if(!hasPermission) {
                if(permissionId.endsWith(".all")) {
                    String sub = permissionId.replaceAll(".all", "");

                    int index = sub.lastIndexOf(".");
                    if(index != -1) {
                        sub = sub.substring(0, index);
                        sub += ".all";

                        hasPermission = hasPermission(sub);
                    }
                }
                else {
                    int index = permissionId.lastIndexOf(".");
                    if(index != -1) {
                        String sub = permissionId.substring(0, index);
                        sub += ".all";
                        hasPermission = hasPermission(sub);
                    }
                }
            }
        }

        return hasPermission;
    }

    public boolean hasOneOfPermissions(List<String> permissions) {
        boolean hasPermission = false;
        for(String permission : permissions) {
            if(hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        return hasPermission;
    }

}
