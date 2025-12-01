package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMDrawingTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMDrawingTemplateRepository extends JpaRepository<PDMDrawingTemplate, Integer> {
    List<PDMDrawingTemplate> findAllByOrderByCreatedDateDesc();
}
