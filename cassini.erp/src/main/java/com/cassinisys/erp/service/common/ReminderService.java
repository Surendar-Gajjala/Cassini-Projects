package com.cassinisys.erp.service.common;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPReminder;
import com.cassinisys.erp.repo.common.ReminderRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nageshreddy on 07-10-2015.
 */
@Service
@Transactional
public class ReminderService implements CrudService<ERPReminder, Integer>,
        PageableService<ERPReminder, Integer> {

    @Autowired
    private ReminderRepository reminderRepository;

    @Override
    public ERPReminder create(ERPReminder erpReminder) {
        return reminderRepository.save(erpReminder);
    }

    @Override
    public ERPReminder get(Integer remainderId) {
        return reminderRepository.findOne(remainderId);
    }

    @Override
    public ERPReminder update(ERPReminder erpReminder) {
        checkNotNull(erpReminder);
        checkNotNull(erpReminder.getId());
        if (reminderRepository.findOne(erpReminder.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return reminderRepository.save(erpReminder);
    }

    @Override
    public List<ERPReminder> getAll() {
        return reminderRepository.findAll();
    }

    @Override
    public void delete(Integer remainderId) {
        checkNotNull(remainderId);
        reminderRepository.delete(remainderId);
    }

    @Override
    public Page<ERPReminder> findAll(Pageable pageable) {
        return reminderRepository.findAll(pageable);
    }
}
