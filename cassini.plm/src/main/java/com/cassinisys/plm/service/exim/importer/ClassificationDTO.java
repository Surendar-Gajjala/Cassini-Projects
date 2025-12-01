package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.plm.model.cm.PLMChangeType;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartType;
import com.cassinisys.plm.model.mfr.PLMManufacturerType;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.rm.RequirementType;
import com.cassinisys.plm.model.rm.SpecificationType;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ClassificationDTO implements Serializable {
    @JsonProperty("item")
    private List<PLMItemType> itemTypes = new ArrayList<>();

    @JsonProperty("change")
    private List<PLMChangeType> changeTypes = new ArrayList<>();

    @JsonProperty("requirement")
    private List<RequirementType> requirementTypes = new ArrayList<>();

    @JsonProperty("specification")
    private List<SpecificationType> specificationTypes = new ArrayList<>();

    @JsonProperty("manufacturer")
    private List<PLMManufacturerType> manufacturerTypes = new ArrayList<>();

    @JsonProperty("manufacturerpart")
    private List<PLMManufacturerPartType> manufacturerPartTypes = new ArrayList<>();

    @JsonProperty("workflow")
    private List<PLMWorkflowType> workflowTypes = new ArrayList<>();
}
