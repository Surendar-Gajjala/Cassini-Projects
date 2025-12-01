package com.cassinisys.plm.model.plm;

import java.io.Serializable;

/**
 * Created by GSR on 18-05-2017.
 */
public enum LifeCyclePhaseType implements Serializable {
    PRELIMINARY,
    REVIEW,
    APPROVED,
    RELEASED,
    OBSOLETE,
    CANCELLED
}
