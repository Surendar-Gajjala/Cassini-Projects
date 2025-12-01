package com.cassinisys.plm.model.wf.dto;

import com.cassinisys.plm.model.wf.PLMWorkFlowStatusAcknowledger;
import com.cassinisys.plm.model.wf.PLMWorkFlowStatusApprover;
import com.cassinisys.plm.model.wf.PLMWorkFlowStatusObserver;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 26-07-2021.
 */
@Data
public class WorkflowStatusAssignmentsDto {
    List<PLMWorkFlowStatusApprover> approvers = new LinkedList<>();
    List<PLMWorkFlowStatusObserver> observers = new LinkedList<>();
    List<PLMWorkFlowStatusAcknowledger> acknowledgers = new LinkedList<>();
}
