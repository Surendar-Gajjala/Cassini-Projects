package com.cassinisys.erp.service.common;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPDistrict;
import com.cassinisys.erp.repo.common.DistrictRepository;

@Service
@Transactional
public class DistrictService {

    @Autowired
    DistrictRepository districtRepo;


    public ERPDistrict createDistrict(ERPDistrict district) {
        return districtRepo.save(district);
    }

    public List<ERPDistrict> getAllDistricts() {
        return districtRepo.findAll();
    }


    public ERPDistrict updateDistrict(ERPDistrict district) {

        checkNotNull(district);
        checkNotNull(district.getId());
        if (districtRepo.findOne(district.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return districtRepo.save(district);

    }

    public List<ERPDistrict> getDistrictByState(Integer state) {
        return districtRepo.getDistrictByState(state);
    }


    public ERPDistrict getDistrictById(Integer districtId) {
        return districtRepo.findOne(districtId);
    }


    public void deleteDistrict(Integer districtId) {
        checkNotNull(districtId);
        districtRepo.delete(districtId);
    }

}
