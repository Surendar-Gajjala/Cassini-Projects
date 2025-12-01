package com.cassinisys.erp.security;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.model.security.ERPLogin;
import com.cassinisys.erp.model.security.ERPPermission;
import com.cassinisys.erp.model.security.ERPRole;
import com.cassinisys.erp.repo.security.LoginRoleRepository;
import com.cassinisys.erp.repo.security.RolePermissionRepository;
import com.cassinisys.erp.service.security.SecurityService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

/**
 * Created by reddy on 9/8/15.
 */
public class SecurityTest extends BaseTest {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private LoginRoleRepository loginRoleRepository;

    @Test
    @Rollback(false)
    public void addRole() throws Exception {
        ERPLogin login = securityService.getLoginByLoginName("admin");
        if(login != null) {
            ERPRole role = new ERPRole();
            role.setName("Sales Rep");
            role.setDescription("Sales rep");
            role = securityService.createRole(role);

            ERPPermission permission = securityService.getPermissionById("permission.crm.all");
            securityService.addRolePermission(role, permission);

            securityService.addLoginRole(login, role);
        }
    }
    @Test
    public void getRole() throws Exception {
        ERPLogin login = securityService.getLoginByLoginName("admin");
        System.out.println(login);
    }

    @Test
    @Rollback(false)
    public void removeRole() throws Exception {
        ERPRole role = securityService.getRoleByName("Sales Rep");
        if(role != null) {
            securityService.deleteRole(role);
        }
    }

    @Test
    public void getRolesAndPermissions() throws Exception {
        List<ERPRole> roles = securityService.getAllRoles();
        for(ERPRole role : roles) {
            System.out.println(role.getName());
            List<ERPPermission> permissions = role.getPermissions();
            for(ERPPermission permission : permissions) {
                System.out.println("\t" + permission.getId());
            }
        }
    }


    @Test
    public void testRolesWithPermission() throws Exception {
        List<String> permissions = Arrays.asList("permission.all", "permission.crm.all",
                "permission.crm.order.all", "permission.crm.order.approve");
        List<ERPRole> roles = rolePermissionRepository.getRolesWithPermission(permissions);
        List<ERPLogin> logins = loginRoleRepository.findLoginsByRoles(roles);

        for(ERPLogin login : logins) {
            System.out.println("\n");
            System.out.println(login.getLoginName());
            System.out.println("\n");
        }
    }
}
