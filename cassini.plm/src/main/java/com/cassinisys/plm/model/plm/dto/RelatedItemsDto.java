package com.cassinisys.plm.model.plm.dto;

import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMRelationship;
import lombok.Data;

/**
 * Created by subramanyam on 16-04-2020.
 */
@Data
public class RelatedItemsDto {

    private Integer id;

    private PLMRelationship relationship;

    private Integer fromItem;

    private String fromItemNumber;

    private String fromItemName;
    private String fromItemDescription;

    private Integer toItem;

    private String toItemNumber;

    private String toItemName;
    private String toItemDescription;

    private PLMItemRevision fromItemRevision;

    private PLMItemRevision toItemRevision;

    private Boolean bothDirection = Boolean.FALSE;

}
