package com.cassinisys.plm.model.pqm.dto;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.model.custom.CustomObjectAttribute;
import com.cassinisys.platform.model.custom.CustomObjectTypeAttribute;
import com.cassinisys.plm.model.cm.PLMChangeAttribute;
import com.cassinisys.plm.model.cm.PLMChangeTypeAttribute;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESObjectTypeAttribute;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.mro.MROObjectTypeAttribute;
import com.cassinisys.plm.model.pgc.PGCObjectAttribute;
import com.cassinisys.plm.model.pgc.PGCObjectTypeAttribute;
import com.cassinisys.plm.model.plm.PLMItemAttribute;
import com.cassinisys.plm.model.plm.PLMItemRevisionAttribute;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import com.cassinisys.plm.model.pm.PMObjectTypeAttribute;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.req.PLMRequirementObjectAttribute;
import com.cassinisys.plm.model.req.PLMRequirementObjectTypeAttribute;
import com.cassinisys.plm.model.rm.RmObjectAttribute;
import com.cassinisys.plm.model.rm.RmObjectTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflowActivityFormData;
import com.cassinisys.plm.model.wf.PLMWorkflowActivityFormFields;
import com.cassinisys.plm.model.wf.PLMWorkflowAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflowTypeAttribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 12-06-2020.
 */
@Data
public class QualityTypeAttributeDto {

    public List<ObjectAttribute> objectAttributes = new ArrayList<>();
    public List<ObjectAttribute> revisionObjectAttributes = new ArrayList<>();
    public List<ObjectTypeAttribute> objectTypeAttributes = new ArrayList<>();
    private PQMProblemReport problemReport;
    private PQMNCR ncr;
    private PQMQCR qcr;
    private PQMPPAP ppap;
    private PQMSupplierAudit supplierAudit;
    private PQMInspectionPlan inspectionPlan;
    private PQMProductInspectionPlan productInspectionPlan;
    private PQMMaterialInspectionPlan materialInspectionPlan;
    private PQMInspection inspection;
    private PQMItemInspection itemInspection;
    private PQMMaterialInspection materialInspection;
    private List<PQMInspectionPlanAttribute> inspectionPlanAttributes = new ArrayList<>();
    private List<PQMInspectionPlanRevisionAttribute> inspectionPlanRevisionAttributes = new ArrayList<>();
    private List<PQMInspectionAttribute> inspectionAttributes = new ArrayList<>();
    private List<PQMProblemReportAttribute> problemReportAttributes = new ArrayList<>();
    private List<PQMNCRAttribute> ncrAttributes = new ArrayList<>();
    private List<PQMQCRAttribute> qcrAttributes = new ArrayList<>();
    private List<PQMPPAPAttribute> ppapAttributes = new ArrayList<>();
    private List<PQMSupplierAuditAttribute> supplierAuditAttributes = new ArrayList<>();
    private List<PQMQualityTypeAttribute> qualityTypeAttributes = new ArrayList<>();
    private List<PLMChangeTypeAttribute> changeTypeAttributes = new ArrayList<>();
    private List<PLMChangeAttribute> changeAttributes = new ArrayList<>();
    private List<PLMItemTypeAttribute> itemTypeAttributes = new ArrayList<>();
    private List<PLMItemAttribute> itemAttributes = new ArrayList<>();
    private List<PLMItemRevisionAttribute> itemRevisionAttributes = new ArrayList<>();
    private List<PLMManufacturerTypeAttribute> manufacturerTypeAttributes = new ArrayList<>();
    private List<PLMManufacturerAttribute> manufacturerAttributes = new ArrayList<>();
    private List<PLMManufacturerPartTypeAttribute> partTypeAttributes = new ArrayList<>();
    private List<PLMManufacturerPartAttribute> partAttributes = new ArrayList<>();
    private List<RmObjectTypeAttribute> rmObjectTypeAttributes = new ArrayList<>();
    private List<RmObjectAttribute> rmObjectAttributes = new ArrayList<>();
    private List<CustomObjectTypeAttribute> customObjectTypeAttributes = new ArrayList<>();
    private List<CustomObjectAttribute> customObjectAttributes = new ArrayList<>();
    private List<MESObjectTypeAttribute> mesObjectTypeAttributes = new ArrayList<>();
    private List<MESObjectAttribute> mesObjectAttributes = new ArrayList<>();
    private List<MROObjectTypeAttribute> mroObjectTypeAttributes = new ArrayList<>();
    private List<MROObjectAttribute> mroObjectAttributes = new ArrayList<>();
    private List<PLMSupplierTypeAttribute> supplierTypeAttributes = new ArrayList<>();
    private List<PLMSupplierAttribute> supplierAttributes = new ArrayList<>();
    private List<PGCObjectTypeAttribute> pgcObjectTypeAttributes = new ArrayList<>();
    private List<PGCObjectAttribute> pgcObjectAttributes = new ArrayList<>();
    private List<PLMRequirementObjectTypeAttribute> requirementObjectTypeAttributes = new ArrayList<>();
    private List<PLMRequirementObjectAttribute> requirementObjectAttributes = new ArrayList<>();
    private List<PLMWorkflowTypeAttribute> workflowTypeAttributes = new ArrayList<>();
    private List<PLMWorkflowAttribute> workflowAttributes = new ArrayList<>();
    private List<PLMWorkflowActivityFormFields> workflowActivityFormFields = new ArrayList<>();
    private List<PLMWorkflowActivityFormData> workflowActivityFormData = new ArrayList<>();
    private List<PMObjectTypeAttribute> pmObjectTypeAttributes = new ArrayList<>();
}
