package com.cassinisys.plm.model.req;

import com.cassinisys.plm.model.pm.PMObjectType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 23-10-2020.
 */
@Data
public class PMObjectTypesDto {

    List<PMObjectType> pmObjectTypes = new ArrayList<>();

    List<PLMRequirementDocumentType> requirementDocumentTypes = new ArrayList<>();

    List<PLMRequirementType> requirementTypes = new ArrayList<>();
}
