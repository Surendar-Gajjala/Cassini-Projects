package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import lombok.Data;

/**
 * Created by CassiniSystems on 12-06-2020.
 */
@Data
public class DCRRelatedItemDto {

	private PLMItem item;

	private PLMItemRevision itemRevision;

	private Integer dcr;

	private Integer id;


}
