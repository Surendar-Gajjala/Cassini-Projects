package com.cassinisys.erp.repo.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.common.ERPTodoList;

@Repository
public interface TodoListRepository extends JpaRepository<ERPTodoList, Integer> {

}
