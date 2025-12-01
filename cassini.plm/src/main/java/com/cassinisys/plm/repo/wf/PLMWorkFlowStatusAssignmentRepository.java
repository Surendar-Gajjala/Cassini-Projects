package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkFlowStatusAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@Repository
public interface PLMWorkFlowStatusAssignmentRepository extends JpaRepository<PLMWorkFlowStatusAssignment, Integer>, QueryDslPredicateExecutor<PLMWorkFlowStatusAssignment> {

    List<PLMWorkFlowStatusAssignment> findByStatusOrderByIdDesc(Integer id);

    List<PLMWorkFlowStatusAssignment> findByPerson(Integer id);

    List<PLMWorkFlowStatusAssignment> findByStatusAndIteration(Integer status, Integer iteration);

    List<PLMWorkFlowStatusAssignment> findByStatusAndPersonOrderByIdDesc(Integer status, Integer person);

    List<PLMWorkFlowStatusAssignment> findByStatus(Integer statusId);
}
