package com.cassinisys.erp.service.common;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPNote;
import com.cassinisys.erp.repo.common.NoteRepository;
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
public class NoteService implements CrudService<ERPNote, Integer>,
        PageableService<ERPNote, Integer> {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public ERPNote create(ERPNote erpNote) {
        return noteRepository.save(erpNote);
    }

    @Override
    public ERPNote get(Integer noteId) {
        return noteRepository.findOne(noteId);
    }

    @Override
    public ERPNote update(ERPNote erpNote) {
        checkNotNull(erpNote);
        checkNotNull(erpNote.getId());
        if (noteRepository.findOne(erpNote.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return noteRepository.save(erpNote);
    }

    @Override
    public List<ERPNote> getAll() {
        return noteRepository.findAll();
    }

    @Override
    public void delete(Integer noteId) {
        checkNotNull(noteId);
        noteRepository.delete(noteId);
    }

    @Override
    public Page<ERPNote> findAll(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }
}