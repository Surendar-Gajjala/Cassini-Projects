package com.cassinisys.plm.model.dto;

import lombok.Data;

/**
 * Created by subramanyam on 19-07-2020.
 */
@Data
public class PersonSharedObjectCounts {
    private Integer project = 0;
    private Integer program = 0;
    private Integer activity = 0;
    private Integer task = 0;
    private Integer item = 0;
    private Integer declaration = 0;
    private Integer mfrPart = 0;
    private Integer mfr = 0;
    private Integer supplier = 0;
    private Integer folder = 0;
    private Integer customObject = 0;
    private Integer sharedFolders = 0;

}
