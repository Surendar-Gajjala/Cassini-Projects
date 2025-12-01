package com.cassinisys.erp.service.production;

import com.cassinisys.erp.api.filtering.MaterialDailyIssueReportPredicateBuilder;
import com.cassinisys.erp.model.api.criteria.MaterialDailyIssueReportCriteria;
import com.cassinisys.erp.model.production.ERPMaterialDailyIssueReport;
import com.cassinisys.erp.model.production.QERPMaterialDailyIssueReport;
import com.cassinisys.erp.repo.production.MaterialDailyIssueReportRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Nageshreddy on 21-08-2018.
 */

@Service
@Transactional
public class MaterialDailyIssueReportService implements CrudService<ERPMaterialDailyIssueReport, Integer> {

    @Autowired
    private MaterialDailyIssueReportPredicateBuilder predicateBuilder;

    @Autowired
    private MaterialDailyIssueReportRepository dailyHistoryRepository;

    @Override
    public ERPMaterialDailyIssueReport create(ERPMaterialDailyIssueReport erpMaterialDailyHistory) {
        return dailyHistoryRepository.save(erpMaterialDailyHistory);
    }

    @Override
    public ERPMaterialDailyIssueReport update(ERPMaterialDailyIssueReport erpMaterialDailyHistory) {
        ERPMaterialDailyIssueReport dailyHistory = dailyHistoryRepository.save(erpMaterialDailyHistory);
        return dailyHistory;
    }

    public ERPMaterialDailyIssueReport addQuantity(Integer material, Integer quantity) {
//        Date date = new Date(118, 7, 22);//we get 2018-08-22
        Date date = new Date();
        ERPMaterialDailyIssueReport dailyHistory = null;
        dailyHistory = dailyHistoryRepository.findByMaterialAndTimestamp(material, date);
        if (dailyHistory != null) {
            dailyHistory.setQuantity(dailyHistory.getQuantity() + quantity);
            dailyHistory.setRemainingQty(dailyHistory.getRemainingQty() + quantity);
        } else {
            dailyHistory = new ERPMaterialDailyIssueReport();
            dailyHistory.setQuantity(quantity);
            dailyHistory.setRemainingQty(quantity);
            dailyHistory.setMaterial(material);
            dailyHistory.setTimestamp(date);
            dailyHistory = dailyHistoryRepository.save(dailyHistory);
        }
        return dailyHistory;
    }

    //To get yesterday Date
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @Override
    public void delete(Integer integer) {
        dailyHistoryRepository.delete(integer);
    }

    @Override
    public ERPMaterialDailyIssueReport get(Integer integer) {
        return dailyHistoryRepository.getOne(integer);
    }

    @Override
    public List<ERPMaterialDailyIssueReport> getAll() {
        return dailyHistoryRepository.findAll();
    }

    public Page<ERPMaterialDailyIssueReport> getAll(Pageable pageable, MaterialDailyIssueReportCriteria criteria) {
        Predicate predicate = predicateBuilder.build(criteria,
                QERPMaterialDailyIssueReport.eRPMaterialDailyIssueReport);
        return dailyHistoryRepository.findAll(predicate, pageable);
    }

}
