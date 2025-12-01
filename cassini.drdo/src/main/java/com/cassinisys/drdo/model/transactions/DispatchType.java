package com.cassinisys.drdo.model.transactions;

import java.io.Serializable;

/**
 * Created by subra on 04-02-2019.
 */
public enum DispatchType implements Serializable {
    RETURN,
    FAILURE,
    FABRICATION,
    REJECTED
}
