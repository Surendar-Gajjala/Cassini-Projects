package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMBOMOccurrence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMBOMOccurrenceRepository extends JpaRepository<PDMBOMOccurrence, Integer> {
    List<PDMBOMOccurrence> findByParent(Integer parentId);
    List<PDMBOMOccurrence> findByParentOrderById(Integer parentId);
    List<PDMBOMOccurrence> findByIdIn(Iterable<Integer> ids);
}
