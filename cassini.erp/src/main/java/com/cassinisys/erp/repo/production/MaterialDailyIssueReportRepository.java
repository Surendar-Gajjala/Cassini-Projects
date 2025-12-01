package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPMaterialDailyIssueReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Date;

/**
 * Created by Nageshreddy on 21-08-2018.
 */
public interface MaterialDailyIssueReportRepository extends JpaRepository<ERPMaterialDailyIssueReport, Integer>, QueryDslPredicateExecutor<ERPMaterialDailyIssueReport> {

    ERPMaterialDailyIssueReport findByMaterialAndTimestamp(Integer material, Date timeStamp);

}
