package com.cassinisys.erp.service.common;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPTodoList;
import com.cassinisys.erp.repo.common.TodoListRepository;
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
public class TodoListService implements CrudService<ERPTodoList, Integer>,
        PageableService<ERPTodoList, Integer> {

    @Autowired
    TodoListRepository todoListRepository;

    @Override
    public ERPTodoList create(ERPTodoList erpTodoList) {
        return todoListRepository.save(erpTodoList);
    }

    @Override
    public ERPTodoList update(ERPTodoList erpTodoList) {
        checkNotNull(erpTodoList);
        checkNotNull(erpTodoList.getId());
        if (todoListRepository.findOne(erpTodoList.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return todoListRepository.save(erpTodoList);
    }

    @Override
    public void delete(Integer integer) {
        todoListRepository.delete(integer);
    }

    @Override
    public ERPTodoList get(Integer integer) {
        return todoListRepository.findOne(integer);
    }

    @Override
    public List<ERPTodoList> getAll() {
        return todoListRepository.findAll();
    }

    @Override
    public Page<ERPTodoList> findAll(Pageable pageable) {
        return todoListRepository.findAll(pageable);
    }
}
