package com.cassinisys.erp.service.common;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPMessage;
import com.cassinisys.erp.repo.common.MessageRepository;
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
 * Created by Rajabrahmachary on 07-10-2015.
 */
@Service
@Transactional
public class MessageService implements CrudService<ERPMessage, Integer>,
        PageableService<ERPMessage, Integer> {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public ERPMessage create(ERPMessage erpMessage) {
        return messageRepository.save(erpMessage);
    }

    @Override
    public ERPMessage update(ERPMessage erpMessage) {
        checkNotNull(erpMessage);
        checkNotNull(erpMessage.getId());
        if (messageRepository.findOne(erpMessage.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return messageRepository.save(erpMessage);
    }

    @Override
    public void delete(Integer integer) {
        checkNotNull(integer);
        messageRepository.delete(integer);
    }

    @Override
    public ERPMessage get(Integer integer) {
        return messageRepository.findOne(integer);
    }

    @Override
    public List<ERPMessage> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public Page<ERPMessage> findAll(Pageable pageable) {
        return messageRepository.findAll(pageable);
    }
}
