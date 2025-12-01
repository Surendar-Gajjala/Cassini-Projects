package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.plm.model.pdm.PDMFileType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
public class PDMFileVersionCriteria extends Criteria implements Serializable {
    private String name;
    private String description;
    private PDMFileType fileType;
    private Boolean latest = Boolean.TRUE;
    private String searchQuery;
}
