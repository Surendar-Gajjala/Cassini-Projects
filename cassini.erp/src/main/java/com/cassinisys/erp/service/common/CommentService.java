package com.cassinisys.erp.service.common;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPComment;
import com.cassinisys.erp.repo.common.CommentRepository;
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
public class CommentService implements CrudService<ERPComment, Integer>,
        PageableService<ERPComment, Integer> {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public ERPComment create(ERPComment erpComment) {
        return commentRepository.save(erpComment);
    }

    @Override
    public ERPComment get(Integer commentId) {
        return commentRepository.findOne(commentId);
    }

    @Override
    public ERPComment update(ERPComment erpComment) {
        checkNotNull(erpComment);
        checkNotNull(erpComment.getId());
        if (commentRepository.findOne(erpComment.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return commentRepository.save(erpComment);
    }

    @Override
    public List<ERPComment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public void delete(Integer commentId) {
        checkNotNull(commentId);
        commentRepository.delete(commentId);
    }

    @Override
    public Page<ERPComment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }
}
