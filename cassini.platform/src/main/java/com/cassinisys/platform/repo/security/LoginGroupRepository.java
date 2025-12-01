package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.security.LoginGroup;
import com.cassinisys.platform.model.security.LoginGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 10/20/2016.
 */
@Repository
public interface LoginGroupRepository extends JpaRepository<LoginGroup, LoginGroupId> {
    @Query(
            "SELECT lg FROM LoginGroup lg WHERE lg.id.login = :login"
    )
    List<LoginGroup> findLoginGroups(@Param("login") Login login);

    @Modifying
    @Query (
            "DELETE FROM LoginGroup lg WHERE lg.id.login = :login"
    )
    void deleteAllByLogin(@Param("login") Login login);

    @Modifying
    @Query (
            "DELETE FROM LoginGroup lg WHERE lg.id.group = :group"
    )
    void deleteAllByGroup(@Param("group") PersonGroup group);
}