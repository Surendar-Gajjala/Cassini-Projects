package com.cassinisys.erp.repo.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.common.ERPNote;

@Repository
public interface NoteRepository extends JpaRepository<ERPNote, Integer> {

}
