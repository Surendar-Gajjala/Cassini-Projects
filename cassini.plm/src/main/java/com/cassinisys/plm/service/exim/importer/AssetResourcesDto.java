package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.plm.model.mes.MESType;
import lombok.Data;

/**
 * Created by CassiniSystems on 22-11-2021.
 */
@Data
public class AssetResourcesDto {

   private Integer id;
   private String name;
   private Integer number;
   private MESType type;
}
