package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import lombok.Data;

/**
 * Created by CassiniSystems on 12-06-2020.
 */
@Data
public class MCORelatedItemDto {

    private PLMItem item;

    private PLMItemRevision itemRevision;

    private Integer mco;

    private Integer id;

    private Integer material;


}
