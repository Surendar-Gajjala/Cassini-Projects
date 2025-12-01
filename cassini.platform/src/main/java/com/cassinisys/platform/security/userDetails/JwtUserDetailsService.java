package com.cassinisys.platform.security.userDetails;

import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.security.SecurityPermission;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.repo.security.LoginSecurityPermissionRepository;
import com.cassinisys.platform.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
    private LoginRepository loginRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private LoginSecurityPermissionRepository loginSecurityPermissionRepository;

	@Override
    @Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login login = loginRepository.findByLoginName(username);
        if (login == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        if(login.getExternal()){
            login.setGroupSecurityPermissions(securityService.loadExternalPermission(login));
            login.getGroupSecurityPermissions().addAll(securityService.loadPersonSecurityPermissions(login));
            login.getLoginSecurityPermissions().addAll(loginSecurityPermissionRepository.findByPerson(login.getPerson()));
        } else {
            login.setGroupSecurityPermissions(securityService.loadPersonSecurityPermissions(login));
            login.getLoginSecurityPermissions().addAll(loginSecurityPermissionRepository.findByPerson(login.getPerson()));
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        login.getGroups().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return login;
    }

}