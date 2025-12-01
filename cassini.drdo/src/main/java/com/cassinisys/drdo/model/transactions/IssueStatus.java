package com.cassinisys.drdo.model.transactions;

import java.io.Serializable;

/**
 * Created by subramanyam on 09-07-2019.
 */
public enum IssueStatus implements Serializable {
    NEW,
    BDL_QC,
    STORE,
    ISSUED,
    APPROVED,
    PARTIALLY_RECEIVED,
    PARTIALLY_APPROVED,
    RECEIVED,
    REJECTED,
    PARTIALLY_REJECTED,
    BDL_PPC,
    VERSITY_QC,
    VERSITY_PPC,
    ITEM_RESET
}
