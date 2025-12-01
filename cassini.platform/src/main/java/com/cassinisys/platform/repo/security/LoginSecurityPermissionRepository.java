package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.security.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by lakshmi on 10/20/2016.
 */
@Repository
public interface LoginSecurityPermissionRepository extends JpaRepository<LoginSecurityPermission, Integer> {

    List<LoginSecurityPermission> findByPerson(Person person);
    List<Person> findByPermissionId(Integer permissionId);
    @Query(
            "SELECT rp FROM LoginSecurityPermission rp WHERE rp.person.id IN :personIds"
    )
    List<LoginSecurityPermission> findBypersonIds(@Param("personIds") Set<Integer> personIds);
    List<LoginSecurityPermission> findByObjectType(String objectType);
    List<LoginSecurityPermission> findByObjectTypeAndPrivilegeAndCriteria(String objectType, String privilege, String criteria);
}
