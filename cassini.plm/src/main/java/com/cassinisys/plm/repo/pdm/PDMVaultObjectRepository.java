package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMVaultObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDMVaultObjectRepository extends JpaRepository<PDMVaultObject, Integer> {
}
