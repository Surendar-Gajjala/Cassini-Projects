package com.cassinisys.platform.repo.security;


import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.security.LoginRole;
import com.cassinisys.platform.model.security.LoginRoleId;
import com.cassinisys.platform.model.security.Role;
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
public interface LoginRoleRepository extends JpaRepository<LoginRole, LoginRoleId> {
    @Query(
            "SELECT lr FROM LoginRole lr WHERE lr.id.login = :login"
    )
    List<LoginRole> findLoginRoles(@Param("login") Login login);

    @Modifying
    @Query (
            "DELETE FROM LoginRole lr WHERE lr.id.login = :login"
    )
    void deleteAllByLogin(@Param("login") Login login);

    @Modifying
    @Query (
            "DELETE FROM LoginRole lr WHERE lr.id.role = :role"
    )
    void deleteAllByRole(@Param("role") Role role);
}
