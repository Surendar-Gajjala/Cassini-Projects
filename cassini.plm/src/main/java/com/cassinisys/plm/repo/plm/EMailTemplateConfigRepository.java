package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.EmailTemplateConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EMailTemplateConfigRepository extends JpaRepository<EmailTemplateConfiguration, Integer> {

    EmailTemplateConfiguration findByTemplateName(String templateName);
}