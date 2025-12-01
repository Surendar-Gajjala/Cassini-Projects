package com.cassinisys.erp.repo.security;

import com.cassinisys.erp.model.security.ERPLogin;
import com.cassinisys.erp.model.security.ERPLoginRole;
import com.cassinisys.erp.model.security.ERPRole;
import com.cassinisys.erp.model.security.LoginRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface LoginRoleRepository extends JpaRepository<ERPLoginRole, LoginRoleId> {
    @Query(
            "SELECT lr FROM ERPLoginRole lr WHERE lr.id.login = :login"
    )
    List<ERPLoginRole> findLoginRoles(@Param("login") ERPLogin login);

    @Query(
            "SELECT DISTINCT(lr.id.login) FROM ERPLoginRole lr WHERE lr.id.role IN :roles"
    )
    List<ERPLogin> findLoginsByRoles(@Param("roles") List<ERPRole> roles);

    @Modifying
    @Query (
            "DELETE FROM ERPLoginRole lr WHERE lr.id.login = :login"
    )
    void deleteAllByLogin(@Param("login") ERPLogin login);

    @Modifying
    @Query (
            "DELETE FROM ERPLoginRole lr WHERE lr.id.role = :role"
    )
    void deleteAllByRole(@Param("role") ERPRole role);
}
