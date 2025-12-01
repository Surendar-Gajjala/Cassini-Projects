package com.cassinisys.is.model.workflow;

import java.io.Serializable;

/**
 * Created by CassiniSystems on 19-05-2017.
 */
public enum WorkflowStatusType implements Serializable {
    PENDING,
    REVIEW,
    RELEASED,
    COMPLETE,
    CANCELLED,
    HOLD,
    UNDEFINED
}
