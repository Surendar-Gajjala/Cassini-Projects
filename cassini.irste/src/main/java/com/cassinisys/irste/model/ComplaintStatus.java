package com.cassinisys.irste.model;

import java.io.Serializable;

/**
 * Created by Nageshreddy on 06-11-2018.
 */

public enum ComplaintStatus implements Serializable {
    NEW,
    INPROGRESS,
    AT_ASSISTOR,
    ASSISTED,
    AT_FACILITATOR,
    FACILITATED,
    COMPLETED
}
