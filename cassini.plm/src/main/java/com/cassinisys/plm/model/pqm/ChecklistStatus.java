package com.cassinisys.plm.model.pqm;

import java.io.Serializable;

/**
 * Created by subramanyam on 03-06-2020.
 */
public enum ChecklistStatus implements Serializable {
    PENDING,
    FINISHED,
    HOLD,
    CANCEL
}
