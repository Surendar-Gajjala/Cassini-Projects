package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RmRelationShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RmRelationShipRepository extends JpaRepository<RmRelationShip, Integer> {
}