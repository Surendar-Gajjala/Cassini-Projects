package com.cassinisys.plm.model.rm;

import com.cassinisys.plm.model.plm.PLMFile;
import lombok.Data;

/**
 * Created by subramanyam reddy on 25-09-2018.
 */
@Data
public class CommonFileDto {

    private Integer objectId;

    private Integer parentObjectId;

    private String name;

    private String type;

    private PLMFile file;

    private String parentObjectType;
    private String revision;


}
