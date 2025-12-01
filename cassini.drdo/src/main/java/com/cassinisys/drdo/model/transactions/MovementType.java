package com.cassinisys.drdo.model.transactions;

import java.io.Serializable;

/**
 * Created by subra on 07-10-2018.
 */
public enum MovementType implements Serializable {
    INWARD,
    RECEIVE,
    ISSUE,
    RETURN
}
