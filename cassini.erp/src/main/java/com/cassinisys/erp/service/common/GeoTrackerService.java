package com.cassinisys.erp.service.common;

import com.cassinisys.erp.model.common.ERPGeoTracker;
import com.cassinisys.erp.model.common.QERPGeoTracker;
import com.cassinisys.erp.repo.common.GeoTrackerRepository;
import com.cassinisys.erp.util.Utils;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.DateTimePath;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by reddy on 10/10/15.
 */
@Service
@Transactional
public class GeoTrackerService {

    @Autowired
    private GeoTrackerRepository geoTrackerRepository;

    public List<ERPGeoTracker> getEmployeeGeoLocationsByDate(Integer employeeId, Date date) {
        Date lDate = new DateTime(date).withTime(0, 0, 0, 0).toDate();
        Date rDate = new DateTime(date).withTime(23, 59, 59, 999).toDate();

        DateTimePath<Date> timestamp = QERPGeoTracker.eRPGeoTracker.timestamp;
        Predicate predicate = timestamp.goe(lDate).and(timestamp.loe(rDate));

        predicate = ExpressionUtils.and(predicate, QERPGeoTracker.eRPGeoTracker.employee.eq(employeeId));

        return Utils.makeList(geoTrackerRepository.findAll(predicate));
    }

    public ERPGeoTracker addGeoLocation(ERPGeoTracker geoTracker) {
        return geoTrackerRepository.save(geoTracker);
    }

    public List<ERPGeoTracker> addGeoLocations(List<ERPGeoTracker> geoTracker) {
        return geoTrackerRepository.save(geoTracker);
    }
}
