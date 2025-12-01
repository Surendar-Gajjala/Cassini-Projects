package com.cassinisys.drdo.model.bom;

import java.io.Serializable;

/**
 * Created by subramanyam reddy on 08-11-2018.
 */
public enum BomItemType implements Serializable {
    SECTION,
    SUBSYSTEM,
    UNIT,
    PART,
    COMMONPART
}
