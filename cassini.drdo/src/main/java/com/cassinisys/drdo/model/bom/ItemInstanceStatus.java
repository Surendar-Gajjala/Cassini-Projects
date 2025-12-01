package com.cassinisys.drdo.model.bom;

import java.io.Serializable;

/**
 * Created by subra on 15-10-2018.
 */
public enum ItemInstanceStatus implements Serializable {
    NEW,
    STORE_SUBMITTED,
    ACCEPT,
    P_ACCEPT,
    INVENTORY,
    VERIFIED,
    REVIEW,
    REVIEWED,
    ISSUE,
    TESTED,
    RETURN,
    FAILURE,
    DISPATCH,
    FABRICATION,
    FAILURE_PROCESS,
    P_APPROVED,
    APPROVED,
    REJECTED,
    RESET
}
