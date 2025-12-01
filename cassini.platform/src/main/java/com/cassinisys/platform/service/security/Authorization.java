package com.cassinisys.platform.service.security;

import com.cassinisys.platform.model.core.Session;
import com.cassinisys.platform.model.security.Permission;
import com.cassinisys.platform.model.security.Role;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by reddy on 9/11/15.
 */
public class Authorization implements Serializable {
    private Session session;
    public static final String ADMIN_PERMISSION = "permission.all";

    public Map<String, Role> roles = new HashMap<>();
    public Map<String, Permission> permissions = new HashMap<>();

    public Authorization(Session session) {
        this.session = session;
        init();
    }

    private void init() {
        if (session != null && session.getLogin() != null) {
            List<Role> rList = session.getLogin().getRoles();
            for (Role role : rList) {
                roles.put(role.getName(), role);

                List<Permission> pList = role.getPermissions();
                for (Permission permission : pList) {
                    permissions.put(permission.getId(), permission);
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public boolean isAdministrator() {
        return hasPermission(ADMIN_PERMISSION);
    }

    @Transactional(readOnly = true)
    public boolean hasRole(String roleName) {
        return roles.get(roleName) != null;
    }

    @Transactional(readOnly = true)
    public boolean hasPermission(String permissionId) {
        boolean hasPermission = false;
        if (permissionId.endsWith(".*")) {
            String prefix = permissionId.substring(0, permissionId.length() - 1);
            Set<String> keys = permissions.keySet();
            for (String key : keys) {
                if (key.startsWith(prefix)) {
                    hasPermission = true;
                    break;
                }
            }
        } else { //walk up the hierarchy and check
            hasPermission = permissions.get(permissionId) != null;

            if (!hasPermission) {
                if (permissionId.endsWith(".all")) {
                    String sub = permissionId.replaceAll(".all", "");

                    int index = sub.lastIndexOf(".");
                    if (index != -1) {
                        sub = sub.substring(0, index);
                        sub += ".all";

                        hasPermission = hasPermission(sub);
                    }
                } else {
                    int index = permissionId.lastIndexOf(".");
                    if (index != -1) {
                        String sub = permissionId.substring(0, index);
                        sub += ".all";
                        hasPermission = hasPermission(sub);
                    }
                }
            }
        }

        return hasPermission;
    }

    @Transactional(readOnly = true)
    public boolean hasOneOfPermissions(List<String> permissions) {
        boolean hasPermission = false;
        for (String permission : permissions) {
            if (hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        return hasPermission;
    }

}
