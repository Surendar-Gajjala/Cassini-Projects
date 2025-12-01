package com.cassinisys.plm.model.dto;

import lombok.Data;

/**
 * Created by subramanyam on 20-03-2020.
 */
@Data
public class RevisionDto {

    private Integer id;

    private Integer itemMaster;

    private String revision;

    private Boolean hasBom;

    private Integer instance;


}
