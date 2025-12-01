package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.security.PersonRole;
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
public interface PersonRoleRepository extends JpaRepository<PersonRole, Integer> {

    List<PersonRole> findByRoleId(Integer roleId);
    List<PersonRole> findByPersonId(Integer personId);

    @Modifying
    @Query(
            "DELETE FROM PersonRole prsnRole WHERE prsnRole.personId = :personId and prsnRole.roleId= :roleId "
    )
    void deletePersonRole(@Param("personId") Integer personId,@Param("roleId") Integer roleId);

}
