package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.plm.model.cm.PLMDCRAffectedItem;
import com.cassinisys.plm.model.cm.PLMECRAffectedItem;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 17-06-2020.
 */
@Data
public class RequestedItemDto {

    private PLMDCRAffectedItem affectedItem;

    private PLMECRAffectedItem ecrAffectedItem;

    private String fromRevision;

    private String toRevision;

    private List<String> toRevisions = new ArrayList<>();

    private String Name;

    private String number;

    private PLMLifeCyclePhase fromLifeCycle;

    private PLMLifeCyclePhase toLifeCycle;

    private List<PLMLifeCyclePhase> toLifeCyclePhases = new ArrayList<>();

    private Boolean pendingEco = false;

}
