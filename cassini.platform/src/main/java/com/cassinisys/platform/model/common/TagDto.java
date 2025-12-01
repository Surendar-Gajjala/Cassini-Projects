package com.cassinisys.platform.model.common;

import com.cassinisys.platform.model.core.ObjectType;
import lombok.Data;

/**
 * Created by subramanyam on 20-12-2021.
 */
@Data
public class TagDto {
    Integer id;
    String number;
    String name;
    String description;
    ObjectType objectType;
    Integer object;
    String label;
    Integer revisionId;
}
