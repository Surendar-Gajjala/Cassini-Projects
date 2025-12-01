package com.cassinisys.platform.model.core;

import com.cassinisys.platform.config.LoginEntityListener;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.security.LoginSecurityPermission;
import com.cassinisys.platform.model.security.Permission;
import com.cassinisys.platform.model.security.Role;
import com.cassinisys.platform.model.security.SecurityPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

/**
 * @author reddy
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "LOGIN")
@PrimaryKeyJoinColumn(name = "LOGIN_ID")
@EntityListeners(LoginEntityListener.class)
public class Login extends CassiniObject implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Column(name = "LOGIN_NAME", nullable = false, unique = true)
    private String loginName;

    @OneToOne
    @JoinColumn(name = "PERSON_ID", nullable = false, unique = true)
    private Person person;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "FINGERPRINTDATA")
    private String fingerPrintData;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive = false;

    @Column(name = "IS_LOCKED", nullable = false)
    private boolean isLocked = false;

    @Column(name = "IS_SUPERUSER", nullable = false)
    private Boolean isSuperUser = false;

    @Column(name = "IS_ADMIN", nullable = false)
    private Boolean isAdmin = false;

    @Column(name = "EXTERNAL", nullable = false)
    private boolean external = false;

    @Transient
    private List<Role> roles = new ArrayList<>();


    @Transient
    private List<PersonGroup> groups = new ArrayList<>();

    @Transient
    private Map<String, Boolean> permissions = new HashMap<String, Boolean>();

    @Transient
    private List<Permission> groupPermissions = new ArrayList<>();

    @Transient
    private List<SecurityPermission> groupSecurityPermissions = new ArrayList<>();

    @Transient
    private List<LoginSecurityPermission> loginSecurityPermissions = new ArrayList<>();

    @Transient
    private Map<Integer, List<Permission>> permissionsMap = new HashMap<Integer, List<Permission>>();

    @Transient
    private String username;

    //to track password change
    @Transient
    private Boolean flag;

    @Transient
    private Integer activeUsers;

    @Transient
    private Integer inActiveUsers;

    @Transient
    private Integer activeExternalUsers;

    @Transient
    private Integer inActiveExternalUsers;

    @Transient
    private Integer totalUsers;

    @Transient
    private Boolean existPerson = Boolean.FALSE;

    @Transient
    private Boolean checkedLicence = Boolean.FALSE;

    public Login() {
        super(ObjectType.LOGIN);
    }

    public Map<Integer, List<Permission>> getPermissionsMap() {
        return permissionsMap;
    }

    public void setPermissionsMap(Map<Integer, List<Permission>> permissionsMap) {
        this.permissionsMap = permissionsMap;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        getGroups().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
