package com.cassinisys.is.service.store;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;

/**
 * Created by swapna on 23/10/18.
 */
public class ObjectAtrributeDTO {

    private ObjectAttribute objectAttribute;

    private ObjectTypeAttribute objectTypeAttribute;

    public ObjectAttribute getObjectAttribute() {
        return objectAttribute;
    }

    public void setObjectAttribute(ObjectAttribute objectAttribute) {
        this.objectAttribute = objectAttribute;
    }

    public ObjectTypeAttribute getObjectTypeAttribute() {
        return objectTypeAttribute;
    }

    public void setObjectTypeAttribute(ObjectTypeAttribute objectTypeAttribute) {
        this.objectTypeAttribute = objectTypeAttribute;
    }
}
