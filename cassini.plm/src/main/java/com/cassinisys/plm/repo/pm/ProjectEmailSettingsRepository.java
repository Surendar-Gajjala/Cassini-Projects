package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMProjectEmailSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface ProjectEmailSettingsRepository extends JpaRepository<PLMProjectEmailSetting, Integer>,
        QueryDslPredicateExecutor<PLMProjectEmailSetting> {

    PLMProjectEmailSetting findByProject(Integer project);
}
