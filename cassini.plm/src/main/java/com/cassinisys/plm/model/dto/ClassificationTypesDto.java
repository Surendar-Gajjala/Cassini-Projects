package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.custom.CustomObjectType;
import com.cassinisys.plm.model.cm.PLMChangeType;
import com.cassinisys.plm.model.mes.MESObjectTypesDto;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartType;
import com.cassinisys.plm.model.mfr.PLMManufacturerType;
import com.cassinisys.plm.model.mfr.PLMSupplierType;
import com.cassinisys.plm.model.mro.dto.MROObjectTypesDto;
import com.cassinisys.plm.model.pgc.PGCObjectTypesDto;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.pqm.PQMQualityType;
import com.cassinisys.plm.model.req.PMObjectTypesDto;
import com.cassinisys.plm.model.rm.RmObjectType;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 04-04-2020.
 */
@Data
public class ClassificationTypesDto {

    private List<PLMItemType> itemTypes = new ArrayList<>();
    private List<CustomObjectType> customObjectTypes = new ArrayList<>();
    private List<PLMChangeType> changeTypes = new ArrayList<>();
    private List<PQMQualityType> qualityTypes = new ArrayList<>();
    private PMObjectTypesDto pmObjectTypesDto = new PMObjectTypesDto();
    private MESObjectTypesDto mesObjectTypesDto = new MESObjectTypesDto();
    private MROObjectTypesDto mroObjectTypesDto = new MROObjectTypesDto();
    private List<PLMManufacturerType> manufacturerTypes = new ArrayList<>();
    private List<PLMManufacturerPartType> manufacturerPartTypes = new ArrayList<>();
    private List<PLMSupplierType> supplierTypes = new ArrayList<>();
    private PGCObjectTypesDto pgcObjectTypesDto = new PGCObjectTypesDto();
    private List<PLMWorkflowType> workflowTypes = new ArrayList<>();
    private List<RmObjectType> objectTypes = new ArrayList<>();
}
