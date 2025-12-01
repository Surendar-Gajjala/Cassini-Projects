package com.cassinisys.plm.model.wf;

import java.io.Serializable;

/**
 * Created by CassiniSystems on 19-05-2017.
 */
public enum WorkflowStatusType implements Serializable {
    START,
    NORMAL,
    RELEASED,
    REJECTED,
    FINISH,
    TERMINATE,
    UNDEFINED
}
