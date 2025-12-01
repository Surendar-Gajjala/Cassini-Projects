package com.cassinisys.irste.service;

import com.cassinisys.irste.model.IRSTEComplaintHistory;
import com.cassinisys.irste.repo.IRSTEComplaintHistoryRepository;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Nageshreddy on 06-11-2018.
 */
@Service
public class IRSTEComplaintHistoryService implements CrudService<IRSTEComplaintHistory, Integer> {

    @Autowired
    private IRSTEComplaintHistoryRepository complaintHistoryRepository;

    @Override
    public IRSTEComplaintHistory create(IRSTEComplaintHistory irsteComplaintHistory) {
        return complaintHistoryRepository.save(irsteComplaintHistory);
    }

    @Override
    public IRSTEComplaintHistory update(IRSTEComplaintHistory irsteComplaintHistory) {
        return complaintHistoryRepository.save(irsteComplaintHistory);
    }

    @Override
    public void delete(Integer integer) {
        complaintHistoryRepository.delete(integer);
    }

    @Override
    public IRSTEComplaintHistory get(Integer integer) {
        return complaintHistoryRepository.findOne(integer);
    }

    @Override
    public List<IRSTEComplaintHistory> getAll() {
        return complaintHistoryRepository.findAll();
    }

    public List<IRSTEComplaintHistory> getByComplaintId(Integer id) {
        return complaintHistoryRepository.findByComplaint(id);
    }
}
