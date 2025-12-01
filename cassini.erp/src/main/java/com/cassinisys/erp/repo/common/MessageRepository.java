package com.cassinisys.erp.repo.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.common.ERPMessage;

@Repository
public interface MessageRepository extends JpaRepository<ERPMessage, Integer> {

}
