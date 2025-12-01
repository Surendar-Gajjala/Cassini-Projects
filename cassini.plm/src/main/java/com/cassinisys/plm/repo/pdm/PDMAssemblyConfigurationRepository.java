package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMAssemblyConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMAssemblyConfigurationRepository extends JpaRepository<PDMAssemblyConfiguration, Integer> {
    List<PDMAssemblyConfiguration> findByIdIn(Iterable<Integer> ids);
    List<PDMAssemblyConfiguration> findByAssembly(Integer assemblyId);
}
