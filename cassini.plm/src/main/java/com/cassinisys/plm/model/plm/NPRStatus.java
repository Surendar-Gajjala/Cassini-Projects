package com.cassinisys.plm.model.plm;

import java.io.Serializable;

/**
 * Created by GSR on 18-12-2020.
 */
public enum NPRStatus implements Serializable {
    OPEN,
    PENDING,
    HOLD,
    APPROVED,
    REJECTED
}
