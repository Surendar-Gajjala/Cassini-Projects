package com.cassinisys.is.repo.pm;

import com.cassinisys.is.model.pm.ISProjectPersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 14-05-2019.
 */
@Repository
public interface ProjectPersonRoleRepository extends JpaRepository<ISProjectPersonRole, Integer> {

    List<ISProjectPersonRole> findByProjectAndPerson(Integer projectId, Integer personId);

    List<ISProjectPersonRole> findByProjectAndRole(Integer projectId, Integer roleId);

    ISProjectPersonRole findByProjectAndPersonAndRole(Integer projectId, Integer personId, Integer role);
}
