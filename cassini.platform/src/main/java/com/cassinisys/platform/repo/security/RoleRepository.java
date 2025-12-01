package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);
    List<Role> findAllByOrderByIdAsc();
   }
