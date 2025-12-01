package com.cassinisys.platform.model.dto;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
public class ObjectAttributeDto implements Serializable {
    private CassiniObject cassiniObject;
    private ObjectTypeAttribute objectTypeAttribute;
}
