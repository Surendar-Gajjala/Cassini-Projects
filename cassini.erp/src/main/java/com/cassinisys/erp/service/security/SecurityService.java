package com.cassinisys.erp.service.security;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.security.*;
import com.cassinisys.erp.repo.security.*;
import com.cassinisys.erp.service.notification.sms.NewLoginAccountSms;
import com.cassinisys.erp.service.notification.sms.PasswordResetSms;
import com.cassinisys.erp.service.security.exception.InvalidLoginException;
import com.cassinisys.erp.util.RandomString;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Predicate;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by reddy on 7/27/15.
 */
@Service
@Transactional
public class SecurityService {
    private Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoginRoleRepository loginRoleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private SessionWrapper sessionWrapper;



    public ERPLogin getLoginByLoginName(String loginName) {
        return loginRepository.findByLoginName(loginName);
    }

    public ERPLogin login(String loginName, String password) {
        if(loginName == null || loginName.trim().isEmpty() || loginName.trim().equalsIgnoreCase("null") ||
                password == null || password.trim().isEmpty()  || password.trim().equalsIgnoreCase("null")) {
            throw new InvalidLoginException("User name or password cannot be empty");
        }

        ERPLogin login = loginRepository.findByLoginName(loginName);
        if(login == null) {
            throw new InvalidLoginException("User name does not exist");
        }

        if(!BCrypt.checkpw(password, login.getPassword())) {
            throw new InvalidLoginException("Incorrect password!");
        }

        return login;

    }

    public ERPLogin createLogin(ERPLogin login, String phone, String email) {

        ERPLogin existingLogin = loginRepository.findByPersonId(login.getPerson().getId());

        if(existingLogin != null) {
            throw new ERPException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                    "Employee has already permissions to Login");
        }

        existingLogin = loginRepository.findByLoginName(login.getLoginName());
        if(existingLogin != null) {
            throw new ERPException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                    "Login Name already exists");
        }

        //Generate a random password
        String pwd = RandomString.get().getAlphaNumeric(8);
        //Hash it
        String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
        //Set the hashed password
        login.setPassword(hashed);
        login.setIsActive(true);

        login = loginRepository.save(login);

        //Now send a SMS with the new login and password
        if(phone != null && !phone.trim().isEmpty()) {
            //Remove any spaces
            phone = phone.trim().replaceAll(" ", "");
            //Remove any dashes
            phone = phone.trim().replaceAll("-", "");

            //Make sure +91 is prepended if missing
            if(!phone.startsWith("+91")) {
                phone = "+91" + phone;
            }
            new NewLoginAccountSms(login.getLoginName(), pwd).sendTo(phone);
        }
        return login;
    }

    public ERPLogin changePassword(String oldPassword, String newPassword) {
        oldPassword = oldPassword.trim();
        newPassword = newPassword.trim();

        ERPLogin login = sessionWrapper.getSession().getLogin();
        if(!BCrypt.checkpw(oldPassword, login.getPassword())) {
            throw new InvalidLoginException("Incorrect old password");
        }

        if(oldPassword.equalsIgnoreCase(newPassword)) {
            throw new InvalidLoginException("Old and new passwords cannot be empty");
        }

        //Hash it
        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        //Set the hashed password
        login.setPassword(hashed);

        return loginRepository.save(login);
    }

    public ERPLogin resetPassword(ERPLogin login) {
        //Generate a random password
        String pwd = RandomString.get().getAlphaNumeric(8);
        //Hash it
        String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
        //Set the hashed password
        login.setPassword(hashed);

        login = loginRepository.save(login);

        String phone = login.getPerson().getPhoneMobile();
        //Now send a SMS with the new login and password
        if(phone != null && !phone.trim().isEmpty()) {
            //Remove any spaces
            phone = phone.trim().replaceAll(" ", "");
            //Remove any dashes
            phone = phone.trim().replaceAll("-", "");

            //Make sure +91 is prepended if missing
            if(!phone.startsWith("+91")) {
                phone = "+91" + phone;
            }
            new PasswordResetSms(pwd).sendTo(phone);
        }

        return login;
    }

    public Page<ERPLogin> getLogins(Pageable pageable) {
        return loginRepository.findAll(pageable);
    }

    public ERPPermission getPermissionById(String id) {
        return permissionRepository.findOne(id);
    }

    public List<ERPPermission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public List<ERPRole> getAllRoles() {
        return roleRepository.findAllByOrderByIdAsc();
    }

    public List<ERPRole> saveRoles(List<ERPRole> roles) {
        List<ERPRole> savedRoles = new ArrayList<>();
        for(ERPRole role : roles) {
            savedRoles.add(saveRole(role));
        }
        return savedRoles;
    }

    public ERPRole saveRole(ERPRole role) {
        if(role.getId() != null) {
            deleteRolePermissions(role);
        }
        setRolePermissions(role, role.getPermissions());
        ERPRole savedRole = roleRepository.save(role);
        savedRole.setPermissions(role.getPermissions());
        return savedRole;
    }

    public void deleteRole(Integer id) {
        roleRepository.delete(id);
    }

    public void loadRolePermissions(ERPRole role) {
        List<ERPPermission> permissions = new ArrayList<>();
        List<ERPRolePermission> rolePermissions = getRolePermissions(role);
        for(ERPRolePermission rp : rolePermissions) {
            permissions.add(rp.getId().getPermission());
        }

        role.setPermissions(permissions);
    }


    public void loadLoginRoles(ERPLogin login) {
        List<ERPLoginRole> loginRoles = loginRoleRepository.findLoginRoles(login);
        if(loginRoles.size() > 0) {
            List<ERPRole> roles = new ArrayList<>();
            for(ERPLoginRole loginRole : loginRoles) {
                roles.add(loginRole.getId().getRole());
            }
            login.setRoles(roles);
        }
    }

    public ERPRole createRole(ERPRole role) {
        return roleRepository.save(role);
    }

    public ERPRole updateRole(ERPRole role) {
        role = roleRepository.save(role);
        loadRolePermissions(role);
        return role;
    }

    public void deleteRole(ERPRole role) {
        //First delete all role assignments
        loginRoleRepository.deleteAllByRole(role);

        //Then delete the role permissions
        rolePermissionRepository.deleteRolePermissions(role);

        //Then delete the actual role
        roleRepository.delete(role);
    }


    public List<ERPRolePermission> getRolePermissions(ERPRole role) {
        return rolePermissionRepository.findRolePermissions(role);
    }

    public ERPRolePermission addRolePermission(ERPRole role, ERPPermission permission) {
        ERPRolePermission rolePermission = new ERPRolePermission();
        rolePermission.setId(new RolePermissionId(role, permission));

        return rolePermissionRepository.save(rolePermission);
    }

    public List<ERPRolePermission> setRolePermissions(ERPRole role, List<ERPPermission> permissions) {
        deleteRolePermissions(role);

        List<ERPRolePermission> rolePermissions = new ArrayList<>();

        for(ERPPermission permission : permissions) {
            ERPRolePermission rolePermission = new ERPRolePermission();
            rolePermission.setId(new RolePermissionId(role, permission));

            rolePermissions.add(rolePermission);
        }

        return rolePermissionRepository.save(rolePermissions);
    }

    public void deleteRolePermissions(ERPRole role) {
        rolePermissionRepository.deleteRolePermissions(role);
    }

    public void deleteRolePermission(ERPRole role, ERPPermission permission) {
        ERPRolePermission rolePermission = new ERPRolePermission();
        rolePermission.setId(new RolePermissionId(role, permission));
        rolePermissionRepository.delete(rolePermission);
    }

    public List<ERPLoginRole> getLoginRoles(ERPLogin login) {
        return loginRoleRepository.findLoginRoles(login);
    }

    public List<ERPLoginRole> getLoginRoles(Integer loginId) {
        return loginRoleRepository.findLoginRoles(loginRepository.findOne(loginId));
    }

    public ERPLoginRole addLoginRole(ERPLogin login, ERPRole role) {
        ERPLoginRole loginRole = new ERPLoginRole();
        loginRole.setId(new LoginRoleId(login, role));

        return loginRoleRepository.save(loginRole);
    }

    public List<ERPLoginRole> setLoginRoles(ERPLogin login, List<ERPRole> roles) {

        List<ERPLoginRole> currentLoginRoles = getLoginRoles(login);

        List<ERPLoginRole> loginRoles = new ArrayList<>();
        for(ERPRole role : roles) {
            ERPLoginRole loginRole = new ERPLoginRole();
            loginRole.setId(new LoginRoleId(login, role));
            loginRoles.add(loginRole);
        }

        Map<String, ERPLoginRole> map = new HashMap<>();
        for(ERPLoginRole lr : loginRoles) {
            String key = lr.getId().getLogin().getId() + "-" + lr.getId().getRole().getId();
            map.put(key, lr);
        }

        List<ERPLoginRole> deleted = new ArrayList<>();
        for(ERPLoginRole lr : currentLoginRoles) {
            String key = lr.getId().getLogin().getId() + "-" + lr.getId().getRole().getId();
            if(map.get(key) == null) {
                deleted.add(lr);
            }
        }

        currentLoginRoles.removeAll(deleted);
        currentLoginRoles.addAll(loginRoles);

        loginRoleRepository.delete(deleted);

        return loginRoleRepository.save(loginRoles);
    }

    public List<ERPLoginRole> setLoginRoles(Integer loginId, List<ERPRole> roles) {
        ERPLogin login = loginRepository.findOne(loginId);
        return setLoginRoles(login, roles);
    }

    public void deleteLoginRoles(ERPLogin login) {
        loginRoleRepository.deleteAllByLogin(login);
    }

    public void deleteLoginRole(ERPLogin login, ERPRole role) {
        ERPLoginRole loginRole = new ERPLoginRole();
        loginRole.setId(new LoginRoleId(login, role));
        loginRoleRepository.delete(loginRole);
    }

    public ERPRole getRoleByName(String name) {
        return roleRepository.findByName(name);
    }


    public List<ERPLogin> getLoginsWithPermission(String permission) {
        QERPRolePermission pathBase = QERPRolePermission.eRPRolePermission;
        Predicate predicate = pathBase.id.permission.name.eq(permission);
        return null;
    }


    @PreDestroy
    public void closeAllSessions() {
        LOGGER.info("Closing all cassini sessions...");
        SessionManager.get().invalidateAllSessions();
    }

    @PostConstruct
    public void initialize() {

    }

}
