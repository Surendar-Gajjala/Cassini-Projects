package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMPartBOMOccurrence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMPartBOMOccurrenceRepository extends JpaRepository<PDMPartBOMOccurrence, Integer> {
    List<PDMPartBOMOccurrence> findByIdIn(Iterable<Integer> ids);
    List<PDMPartBOMOccurrence> findByPart(Integer id);
}
