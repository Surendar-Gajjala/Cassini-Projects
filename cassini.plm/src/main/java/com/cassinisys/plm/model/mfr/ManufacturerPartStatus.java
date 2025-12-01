package com.cassinisys.plm.model.mfr;

import java.io.Serializable;

/**
 * Created by Home on 4/25/2016.
 */
public enum ManufacturerPartStatus implements Serializable {
    ACTIVE,
    OBSOLETE,
    ALTERNATE,
    PREFERRED
}
