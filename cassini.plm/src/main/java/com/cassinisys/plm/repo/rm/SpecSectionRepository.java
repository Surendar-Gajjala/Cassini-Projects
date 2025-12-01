package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.SpecSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecSectionRepository extends JpaRepository<SpecSection, Integer> {
    SpecSection findBySpecificationAndId(Integer specId, Integer sectionId);

    List<SpecSection> findBySpecificationAndParentIsNullOrderByCreatedDateAsc(Integer specId);

    List<SpecSection> findByParentOrderByCreatedDateDesc(Integer parent);

    List<SpecSection> findByParentOrderByCreatedDateAsc(Integer parent);
}
