package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMRevisionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDMRevisionMasterRepository extends JpaRepository<PDMRevisionMaster, Integer> {
}
