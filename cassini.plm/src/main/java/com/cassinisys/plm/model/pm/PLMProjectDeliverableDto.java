package com.cassinisys.plm.model.pm;

import com.cassinisys.plm.model.rm.RequirementDeliverable;
import com.cassinisys.plm.model.rm.SpecificationDeliverable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2018.
 */
@Data
public class PLMProjectDeliverableDto {

    private List<PLMDeliverable> itemDeliverables = new ArrayList<>();

    private List<PLMGlossaryDeliverable> glossaryDeliverables = new ArrayList<>();

    private List<SpecificationDeliverable> specificationDeliverables = new ArrayList<>();

    private List<RequirementDeliverable> requirementDeliverables = new ArrayList<>();

    private List<PLMProjectDeliverable> projectItemDeliverables = new ArrayList<>();

    private List<Integer> itemIds = new ArrayList<>();

    private List<Integer> specIds = new ArrayList<>();

    private List<Integer> glossaryIds = new ArrayList<>();

    private List<Integer> requirementIds = new ArrayList<>();


}
