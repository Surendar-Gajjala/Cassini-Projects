package com.cassinisys.drdo.model;

import java.io.Serializable;

/**
 * Created by subramanyam reddy on 02-10-2018.
 */
public enum DRDOObjectType implements Serializable {
    ITEMTYPE,
    ITEMMASTER,
    ITEMREVISION,
    INWARD,
    INWARDITEM,
    REQUEST,
    ISSUE,
    RETURN,
    DISPATCH,
    SUPPLIER,
    PURCHASEORDER,
    FILE,
    TRACKVALUE,
    BOM,
    BOMITEM,
    BOMINSTANCE,
    BOMINSTANCEITEM,
    ITEMINSTANCE,
    INWARDGATEPASS,
    BOMGROUP,
    MANUFACTURER,
    FAILUREVALUELIST,
    BDL_RECEIVE,
    REJECTED
}
