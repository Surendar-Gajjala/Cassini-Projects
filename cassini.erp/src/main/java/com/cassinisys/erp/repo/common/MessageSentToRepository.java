package com.cassinisys.erp.repo.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.common.ERPMessageSentTo;

@Repository
public interface MessageSentToRepository extends
		JpaRepository<ERPMessageSentTo, Integer> {

}
