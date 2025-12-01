package com.cassinisys.drdo.model.transactions;

import java.io.Serializable;

/**
 * Created by subra on 17-10-2018.
 */
public enum RequestStatus implements Serializable {
    REQUESTED,
    BDL_EMPLOYEE,
    BDL_MANAGER,
    CAS_MANAGER,
    PENDING,
    APPROVED,
    RECEIVED,
    REJECTED,
    COLLECTED,
    FINISHED,
    PARTIALLY_ACCEPTED,
    PARTIALLY_APPROVED,
    VERSITY_MANAGER,
    VERSITY_EMPLOYEE,
    CORRECTION
}
