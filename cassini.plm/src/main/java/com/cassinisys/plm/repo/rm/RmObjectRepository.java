package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RmObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RmObjectRepository extends JpaRepository<RmObject, Integer> {
}