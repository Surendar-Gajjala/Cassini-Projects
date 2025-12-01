package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDMConfigurationRepository extends JpaRepository<PDMConfiguration, Integer> {
}
