package com.cassinisys.erp.service.common;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPCountry;
import com.cassinisys.erp.repo.common.CountryRepository;

@Service
@Transactional
public class CountryService {

    @Autowired
    CountryRepository countryRepo;

    public ERPCountry createCountry(ERPCountry state) {
        return countryRepo.save(state);
    }

    public List<ERPCountry> getAllCountries() {
        return countryRepo.findAll();
    }

    public ERPCountry updateCountry(ERPCountry state) {

        checkNotNull(state);
        checkNotNull(state.getId());
        if (countryRepo.findOne(state.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return countryRepo.save(state);

    }

    public ERPCountry getCountryById(Integer stateId) {
        return countryRepo.findOne(stateId);
    }

    public void deleteCountry(Integer stateId) {
        checkNotNull(stateId);
        countryRepo.delete(stateId);
    }

}
