package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementDocumentTemplate;
import com.cassinisys.plm.model.req.PLMRequirementTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 25-06-2021.
 */
@Repository
public interface PLMRequirementTemplateRepository extends JpaRepository<PLMRequirementTemplate, Integer> {

    List<PLMRequirementTemplate> findByDocumentTemplate(PLMRequirementDocumentTemplate documentTemplate);

    List<PLMRequirementTemplate> findByDocumentTemplateAndParentIsNull(PLMRequirementDocumentTemplate documentTemplate);

    List<PLMRequirementTemplate> findByDocumentTemplateAndParentIsNullOrderByCreatedDateAsc(PLMRequirementDocumentTemplate documentTemplate);


    List<PLMRequirementTemplate> findByParent(Integer id);
}
