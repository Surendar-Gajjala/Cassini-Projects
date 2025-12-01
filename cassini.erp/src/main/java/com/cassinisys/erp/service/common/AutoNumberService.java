package com.cassinisys.erp.service.common;

import com.cassinisys.erp.model.common.ERPAutoNumber;
import com.cassinisys.erp.repo.common.AutoNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by reddy on 7/1/15.
 */
@Service
public class AutoNumberService {

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    public List<ERPAutoNumber> getAllAutonumbers() {
        return autoNumberRepository.findAll();
    }

    public ERPAutoNumber getAutoNumberById(Integer id) {
        return autoNumberRepository.findOne(id);
    }

    public ERPAutoNumber save(ERPAutoNumber autoNumber) {
        if(autoNumber.getNextNumber() == -1) {
            autoNumber.setNextNumber(autoNumber.getStart());
        }
        return autoNumberRepository.save(autoNumber);
    }

    public List<ERPAutoNumber> save(List<ERPAutoNumber> autoNumbers) {
        for(ERPAutoNumber auto : autoNumbers) {
            if(auto.getNextNumber() == -1) {
                auto.setNextNumber(auto.getStart());
            }
        }
        return autoNumberRepository.save(autoNumbers);
    }

    public String getNextNumber(Integer autoNumberId) {
        ERPAutoNumber auto = autoNumberRepository.findOne(autoNumberId);
        String next = auto.next();

        autoNumberRepository.save(auto);
        return next;
    }

    public ERPAutoNumber findByName(String name) {
        return autoNumberRepository.findByName(name);
    }

}
