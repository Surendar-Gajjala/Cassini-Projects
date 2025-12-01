package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMPartConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMPartConfigurationRepository extends JpaRepository<PDMPartConfiguration, Integer> {
    List<PDMPartConfiguration> findByIdIn(Iterable<Integer> ids);
    List<PDMPartConfiguration> findByPart(Integer partId);
}
