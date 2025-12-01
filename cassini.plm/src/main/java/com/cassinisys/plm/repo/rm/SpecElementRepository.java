package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.SpecElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecElementRepository extends JpaRepository<SpecElement, Integer> {
    List<SpecElement> findBySpecificationAndParentIsNullOrderByCreatedDateAsc(Integer specId);

    List<SpecElement> findByParentOrderByCreatedDateAsc(Integer parent);

    List<SpecElement> findByParentOrderBySeqNumberAsc(Integer parent);

    List<SpecElement> findByParentOrderByCreatedDateDesc(Integer parent);

    List<SpecElement> findBySpecification(Integer specId);
}