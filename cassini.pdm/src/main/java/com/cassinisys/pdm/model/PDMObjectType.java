package com.cassinisys.pdm.model;

import java.io.Serializable;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
public enum PDMObjectType implements Serializable{
    ITEMTYPE,
    ITEM,
    VAULT,
    FOLDER,
    FILE,
    BOMITEM,
    LOCKABLE,
    COMMIT
}
