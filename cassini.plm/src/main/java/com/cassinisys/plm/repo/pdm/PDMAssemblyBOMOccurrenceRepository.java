package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMAssemblyBOMOccurrence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMAssemblyBOMOccurrenceRepository extends JpaRepository<PDMAssemblyBOMOccurrence, Integer> {
    List<PDMAssemblyBOMOccurrence> findByIdIn(Iterable<Integer> ids);
    List<PDMAssemblyBOMOccurrence> findByAssembly(Integer assyId);

}
