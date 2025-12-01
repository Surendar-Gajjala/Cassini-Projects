package com.cassinisys.drdo.model.transactions;

import java.io.Serializable;

/**
 * Created by subramanyam on 15-07-2019.
 */
public enum IssueItemStatus implements Serializable {
    APPROVED,
    P_APPROVED,
    REJECTED,
    HOLD,
    RECEIVED,
    PENDING
}
