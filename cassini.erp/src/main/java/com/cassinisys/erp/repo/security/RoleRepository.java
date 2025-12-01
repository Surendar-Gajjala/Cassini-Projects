package com.cassinisys.erp.repo.security;

import com.cassinisys.erp.model.security.ERPRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface RoleRepository extends JpaRepository<ERPRole, Integer> {

    ERPRole findByName(String name);
    List<ERPRole> findAllByOrderByIdAsc();
}
