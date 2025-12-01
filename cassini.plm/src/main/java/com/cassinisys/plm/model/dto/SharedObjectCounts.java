package com.cassinisys.plm.model.dto;

import lombok.Data;

/**
 * Created by subramanyam on 19-07-2020.
 */
@Data
public class SharedObjectCounts {
    private long project = 0;
    private long activity = 0;
    private long task = 0;
    private long item = 0;
    private long declaration = 0;
    private long mfrPart = 0;
    private long mfr = 0;
    private long supplier = 0;
    private long folder = 0;
    private long customObject = 0;
    private long sharedFolders = 0;

}
