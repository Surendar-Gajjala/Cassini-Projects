package com.cassinisys.irste.repo;

import com.cassinisys.irste.model.IRSTEComplaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 06-11-2018.
 */
@Repository
public interface IRSTEComplaintRepository extends JpaRepository<IRSTEComplaint, Integer>, QueryDslPredicateExecutor<IRSTEComplaint> {

    IRSTEComplaint findByComplaintNumberIgnoreCase(String complaintNumber);

    Page<IRSTEComplaint> findByPerson(Pageable pageable, Integer person);

    Page<IRSTEComplaint> findByResponder(Pageable pageable, Integer person);

    List<IRSTEComplaint> findByResponder(Integer responder);

    List<IRSTEComplaint> findAllByOrderByIdDesc();
}
