package com.cassinisys.tm.model;

import java.io.Serializable;

/**
 * Created by Rajabrahmachary on 05-07-2016.
 */
public enum TaskStatus implements Serializable {

    ASSIGNED,
    FINISHED,
    VERIFIED,
    APPROVED,
    FINISHEDPENDING,
    VERIFIEDPENDING,
    APPROVEDPENDING,
    REJECTED
}
