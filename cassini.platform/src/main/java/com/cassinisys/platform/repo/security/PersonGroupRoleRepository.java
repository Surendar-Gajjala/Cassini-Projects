package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.security.PersonGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 17-01-2017.
 */

@Repository
public interface PersonGroupRoleRepository extends JpaRepository<PersonGroupRole, Integer> {


    List<PersonGroupRole> findByRoleId(Integer roleId);
    List<PersonGroupRole> findByGroupId(Integer groupId);

    @Modifying
    @Query(
            "DELETE FROM PersonGroupRole personGrpRole WHERE personGrpRole.groupId = :groupId and personGrpRole.roleId= :roleId "
    )
    void deletePersonGroupRole(@Param("groupId") Integer groupId,@Param("roleId") Integer roleId);

}