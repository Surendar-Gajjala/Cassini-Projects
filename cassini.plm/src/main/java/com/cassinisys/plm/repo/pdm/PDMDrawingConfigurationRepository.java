package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMDrawingConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMDrawingConfigurationRepository extends JpaRepository<PDMDrawingConfiguration, Integer> {
    List<PDMDrawingConfiguration> findByIdIn(Iterable<Integer> ids);
}
