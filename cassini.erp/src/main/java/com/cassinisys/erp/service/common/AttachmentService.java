package com.cassinisys.erp.service.common;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPAttachment;
import com.cassinisys.erp.repo.common.AttachmentRepository;
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
public class AttachmentService implements CrudService<ERPAttachment, Integer>,
        PageableService<ERPAttachment, Integer> {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public ERPAttachment create(ERPAttachment erpAttachment) {
        return attachmentRepository.save(erpAttachment);
    }

    @Override
    public ERPAttachment update(ERPAttachment erpAttachment) {
        checkNotNull(erpAttachment);
        checkNotNull(erpAttachment.getId());
        if (attachmentRepository.findOne(erpAttachment.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return attachmentRepository.save(erpAttachment);
    }

    @Override
    public void delete(Integer integer) {
        attachmentRepository.delete(integer);
    }

    @Override
    public ERPAttachment get(Integer integer) {
        return attachmentRepository.findOne(integer);
    }

    @Override
    public List<ERPAttachment> getAll() {
        return attachmentRepository.findAll();
    }

    @Override
    public Page<ERPAttachment> findAll(Pageable pageable) {
        return attachmentRepository.findAll(pageable);
    }
}
