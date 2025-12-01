package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMObjectAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDMObjectAttributeRepository extends JpaRepository<PDMObjectAttribute, Integer> {
}
