package com.cassinisys.plm.service.analytics.oems;

import com.cassinisys.plm.model.analytics.oems.OEMTypeDto;
import com.cassinisys.plm.model.analytics.oems.TopManufacturerDto;
import com.cassinisys.plm.model.analytics.oems.TopProblemPartsDto;
import com.cassinisys.plm.model.analytics.oems.TopRecurringPartsDto;
import com.cassinisys.plm.model.analytics.workflow.WorkflowCountsDto;
import com.cassinisys.plm.model.analytics.workflow.WorkflowTypesDto;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mro.MROEnumObject;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.repo.cm.VarianceAffectedMaterialRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.pqm.NCRProblemItemRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 19-07-2020.
 */
@Service
public class OEMsAnalyticsService {

    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private NCRProblemItemRepository problemItemRepository;
    @Autowired
    private VarianceAffectedMaterialRepository varianceAffectedMaterialRepository;


    @Transactional(readOnly = true)
    public OEMTypeDto getStaticOemDashboardCounts() {
        OEMTypeDto typeDto = new OEMTypeDto();

        typeDto.getPartsByStatus().add(manufacturerPartRepository.getUnqualifiedParts(LifeCyclePhaseType.PRELIMINARY));
        typeDto.getPartsByStatus().add(manufacturerPartRepository.getQualifiedParts(LifeCyclePhaseType.RELEASED));
        typeDto.getPartsByStatus().add(manufacturerPartRepository.getDisqualifiedParts(LifeCyclePhaseType.CANCELLED));
        typeDto.getPartsByStatus().add(manufacturerPartRepository.getObsoleteParts(LifeCyclePhaseType.OBSOLETE));

        typeDto.getMfrByStatus().add(manufacturerRepository.getUnqualifiedMfrs(LifeCyclePhaseType.PRELIMINARY));
        typeDto.getMfrByStatus().add(manufacturerRepository.getReviewMfrs(LifeCyclePhaseType.REVIEW));
        typeDto.getMfrByStatus().add(manufacturerRepository.getApprovedMfrs(LifeCyclePhaseType.RELEASED));
        typeDto.getMfrByStatus().add(manufacturerRepository.getDisqualifiedMfrs(LifeCyclePhaseType.CANCELLED));

        return typeDto;
    }

    @Transactional(readOnly = true)
    public OEMTypeDto getMfrPartsByStatus() {
        OEMTypeDto typeDto = new OEMTypeDto();

        typeDto.getPartsByStatus().add(manufacturerPartRepository.getUnqualifiedParts(LifeCyclePhaseType.PRELIMINARY));
        typeDto.getPartsByStatus().add(manufacturerPartRepository.getQualifiedParts(LifeCyclePhaseType.RELEASED));
        typeDto.getPartsByStatus().add(manufacturerPartRepository.getDisqualifiedParts(LifeCyclePhaseType.CANCELLED));
        typeDto.getPartsByStatus().add(manufacturerPartRepository.getObsoleteParts(LifeCyclePhaseType.OBSOLETE));

        return typeDto;
    }

    @Transactional(readOnly = true)
    public OEMTypeDto getMfrsByStatus() {
        OEMTypeDto typeDto = new OEMTypeDto();

        typeDto.getMfrByStatus().add(manufacturerRepository.getUnqualifiedMfrs(LifeCyclePhaseType.PRELIMINARY));
        typeDto.getMfrByStatus().add(manufacturerRepository.getReviewMfrs(LifeCyclePhaseType.REVIEW));
        typeDto.getMfrByStatus().add(manufacturerRepository.getApprovedMfrs(LifeCyclePhaseType.RELEASED));
        typeDto.getMfrByStatus().add(manufacturerRepository.getDisqualifiedMfrs(LifeCyclePhaseType.CANCELLED));

        return typeDto;
    }


    @Transactional(readOnly = true)
    public List<TopManufacturerDto> getMfrsByParts() {
        List<TopManufacturerDto> typeDto = new ArrayList<>();
        List<Object[]> mfrParts = manufacturerPartRepository.getManufacturerParts();
        for (Object[] row : mfrParts) {
            Integer itemId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            TopManufacturerDto topManufacturerDto = new TopManufacturerDto();
            PLMManufacturer manufacturer = manufacturerRepository.findOne(itemId);
            topManufacturerDto.setId(manufacturer.getId());
            topManufacturerDto.setMfr(manufacturer.getName());
            topManufacturerDto.setParts(count);
            typeDto.add(topManufacturerDto);
        }
        return typeDto;
    }


    @Transactional(readOnly = true)
    public List<TopProblemPartsDto> getProblemParts() {
        List<TopProblemPartsDto> typeDto = new ArrayList<>();
        List<Object[]> problemParts = problemItemRepository.getProblemPartsCounts();
        for (Object[] row : problemParts) {
            TopProblemPartsDto topProblemPartsDto = new TopProblemPartsDto();
            Integer partId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            PLMManufacturerPart part = manufacturerPartRepository.findOne(partId);
            topProblemPartsDto.setPartId(part.getId());
            topProblemPartsDto.setMfrId(part.getManufacturer());
            topProblemPartsDto.setPartNumber(part.getPartNumber());
            topProblemPartsDto.setPartName(part.getPartName());
            topProblemPartsDto.setCount(count);
            typeDto.add(topProblemPartsDto);
        }
        return typeDto;
    }

    @Transactional(readOnly = true)
    public List<TopProblemPartsDto> getProblemMfrs() {
        List<TopProblemPartsDto> typeDto = new ArrayList<>();
        List<Object[]> problemParts = problemItemRepository.getManufacturerCounts();
        for (Object[] row : problemParts) {
            TopProblemPartsDto topProblemPartsDto = new TopProblemPartsDto();
            Integer mfrId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            PLMManufacturer mfr = manufacturerRepository.findOne(mfrId);
            topProblemPartsDto.setMfrId(mfr.getId());
            topProblemPartsDto.setName(mfr.getName());
            topProblemPartsDto.setCount(count);
            typeDto.add(topProblemPartsDto);
        }
        return typeDto;
    }

    @Transactional(readOnly = true)
    public List<TopRecurringPartsDto> getRecurringParts() {
        List<TopRecurringPartsDto> typeDto = new ArrayList<>();
        List<Object[]> problemParts = varianceAffectedMaterialRepository.getRecurringParts();
        for (Object[] row : problemParts) {
            TopRecurringPartsDto topRecurringPartsDto = new TopRecurringPartsDto();
            Integer mfrPartId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            PLMManufacturerPart part = manufacturerPartRepository.findOne(mfrPartId);
            topRecurringPartsDto.setPartId(part.getId());
            topRecurringPartsDto.setMfrId(part.getManufacturer());
            topRecurringPartsDto.setPartNumber(part.getPartNumber());
            topRecurringPartsDto.setCount(count);
            typeDto.add(topRecurringPartsDto);
        }
        return typeDto;
    }

    /* ------------------------------ Workflow Dashboard ------------------------- */
    @Transactional(readOnly = true)
    public WorkflowTypesDto getStaticWorkflowDashboardCounts() {
        WorkflowTypesDto typeDto = new WorkflowTypesDto();
        typeDto.getWorkflowTypes().add(workflowRepository.getNotStartedWfs());
        typeDto.getWorkflowTypes().add(workflowRepository.getNormalWfs());
        typeDto.getWorkflowTypes().add(workflowRepository.getReleasedWfs());
        typeDto.getWorkflowTypes().add(workflowRepository.getRejectedWfs());

        typeDto.getItemTypes().add(workflowRepository.getTypeNotStartedWfs(PLMObjectType.ITEMREVISION));
        typeDto.getItemTypes().add(workflowRepository.getTypeNormalWfs(PLMObjectType.ITEMREVISION));
        typeDto.getItemTypes().add(workflowRepository.getTypeReleasedWfs(PLMObjectType.ITEMREVISION));
        typeDto.getItemTypes().add(workflowRepository.getTypeRejectedWfs(PLMObjectType.ITEMREVISION));

        typeDto.getNprTypes().add(workflowRepository.getTypeNotStartedWfs(PLMObjectType.PLMNPR));
        typeDto.getNprTypes().add(workflowRepository.getTypeNormalWfs(PLMObjectType.PLMNPR));
        typeDto.getNprTypes().add(workflowRepository.getTypeReleasedWfs(PLMObjectType.PLMNPR));
        typeDto.getNprTypes().add(workflowRepository.getTypeRejectedWfs(PLMObjectType.PLMNPR));

        Integer changeNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.CHANGE);
        Integer changeNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.CHANGE);
        Integer changeReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.CHANGE);
        Integer changeRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.CHANGE);

        Integer itemMcoNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.ITEMMCO);
        Integer itemMcoNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.ITEMMCO);
        Integer itemMcoReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.ITEMMCO);
        Integer itemMcoRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.ITEMMCO);

        Integer partMcoStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.OEMPARTMCO);
        Integer partMcoNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.OEMPARTMCO);
        Integer partMcoReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.OEMPARTMCO);
        Integer partMcoRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.OEMPARTMCO);


        typeDto.getChangeTypes().add(changeNotStarted + itemMcoNotStarted + partMcoStarted);
        typeDto.getChangeTypes().add(changeNormal + itemMcoNormal + partMcoNormal);
        typeDto.getChangeTypes().add(changeReleased + itemMcoReleased + partMcoReleased);
        typeDto.getChangeTypes().add(changeRejected + itemMcoRejected + partMcoRejected);


        Integer qcrNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.QCR);
        Integer qcrNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.QCR);
        Integer qcrReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.QCR);
        Integer qcrRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.QCR);


        Integer ncrNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.NCR);
        Integer ncrNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.NCR);
        Integer ncrReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.NCR);
        Integer ncrRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.NCR);

        Integer prNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.PROBLEMREPORT);
        Integer prNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.PROBLEMREPORT);
        Integer prReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.PROBLEMREPORT);
        Integer prRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.PROBLEMREPORT);

        Integer prInspectionNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.INSPECTIONPLANREVISION);
        Integer prInspectionNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.INSPECTIONPLANREVISION);
        Integer prInspectionReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.INSPECTIONPLANREVISION);
        Integer prInspectionRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.INSPECTIONPLANREVISION);

        Integer inspectionNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.MATERIALINSPECTION);
        Integer inspectionNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.MATERIALINSPECTION);
        Integer inspectionReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.MATERIALINSPECTION);
        Integer inspectionRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.MATERIALINSPECTION);

        Integer itemInspectionNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.ITEMINSPECTION);
        Integer itemInspectionNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.ITEMINSPECTION);
        Integer itemInspectionReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.ITEMINSPECTION);
        Integer itemInspectionRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.ITEMINSPECTION);

        typeDto.getQualityTypes().add(qcrNotStarted + ncrNotStarted + prNotStarted + prInspectionNotStarted + inspectionNotStarted + itemInspectionNotStarted);
        typeDto.getQualityTypes().add(qcrNormal + ncrNormal + prNormal + prInspectionNormal + inspectionNormal + itemInspectionNormal);
        typeDto.getQualityTypes().add(qcrReleased + ncrReleased + prReleased + prInspectionReleased + inspectionReleased + itemInspectionReleased);
        typeDto.getQualityTypes().add(qcrRejected + ncrRejected + prRejected + prInspectionRejected + inspectionRejected + itemInspectionRejected);

        typeDto.getMfrTypes().add(workflowRepository.getTypeNotStartedWfs(PLMObjectType.MANUFACTURER));
        typeDto.getMfrTypes().add(workflowRepository.getTypeNormalWfs(PLMObjectType.MANUFACTURER));
        typeDto.getMfrTypes().add(workflowRepository.getTypeReleasedWfs(PLMObjectType.MANUFACTURER));
        typeDto.getMfrTypes().add(workflowRepository.getTypeRejectedWfs(PLMObjectType.MANUFACTURER));

        typeDto.getMfrPartTypes().add(workflowRepository.getTypeNotStartedWfs(PLMObjectType.MANUFACTURERPART));
        typeDto.getMfrPartTypes().add(workflowRepository.getTypeNormalWfs(PLMObjectType.MANUFACTURERPART));
        typeDto.getMfrPartTypes().add(workflowRepository.getTypeReleasedWfs(PLMObjectType.MANUFACTURERPART));
        typeDto.getMfrPartTypes().add(workflowRepository.getTypeRejectedWfs(PLMObjectType.MANUFACTURERPART));

        Integer projectNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.PROJECT);
        Integer projectNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.PROJECT);
        Integer projectReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.PROJECT);
        Integer projectRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.PROJECT);

        Integer activityNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.PROJECTACTIVITY);
        Integer activityNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.PROJECTACTIVITY);
        Integer activityReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.PROJECTACTIVITY);
        Integer activityRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.PROJECTACTIVITY);

        typeDto.getProjectTypes().add(projectNotStarted + activityNotStarted);
        typeDto.getProjectTypes().add(projectNormal + activityNormal);
        typeDto.getProjectTypes().add(projectReleased + activityReleased);
        typeDto.getProjectTypes().add(projectRejected + activityRejected);

      /*  Integer reqNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.REQUIREMENT;
        Integer reqNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.REQUIREMENT;
        Integer reqReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.REQUIREMENT;
        Integer reqRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.REQUIREMENT;

        Integer specNotStarted = workflowRepository.getTypeNotStartedWfs(PLMObjectType.SPECIFICATION;
        Integer specNormal = workflowRepository.getTypeNormalWfs(PLMObjectType.SPECIFICATION;
        Integer specReleased = workflowRepository.getTypeReleasedWfs(PLMObjectType.SPECIFICATION;
        Integer specRejected = workflowRepository.getTypeRejectedWfs(PLMObjectType.SPECIFICATION;

        typeDto.getRequirementTypes().add(reqNotStarted + specNotStarted);
        typeDto.getRequirementTypes().add(reqNormal + specNormal);
        typeDto.getRequirementTypes().add(reqReleased + specReleased);
        typeDto.getRequirementTypes().add(reqRejected + specRejected);*/

        typeDto.getWorkOrderTypes().add(workflowRepository.getTypeNotStartedWfs(PLMObjectType.valueOf(MROEnumObject.MROWORKORDER.toString())));
        typeDto.getWorkOrderTypes().add(workflowRepository.getTypeNormalWfs(PLMObjectType.valueOf(MROEnumObject.MROWORKORDER.toString())));
        typeDto.getWorkOrderTypes().add(workflowRepository.getTypeReleasedWfs(PLMObjectType.valueOf(MROEnumObject.MROWORKORDER.toString())));
        typeDto.getWorkOrderTypes().add(workflowRepository.getTypeRejectedWfs(PLMObjectType.valueOf(MROEnumObject.MROWORKORDER.toString())));

        return typeDto;
    }

    @Transactional(readOnly = true)
    public WorkflowTypesDto getWorkflowByStatus() {
        WorkflowTypesDto typeDto = new WorkflowTypesDto();
        typeDto.getWorkflowTypes().add(workflowRepository.getNotStartedWfs());
        typeDto.getWorkflowTypes().add(workflowRepository.getNormalWfs());
        typeDto.getWorkflowTypes().add(workflowRepository.getReleasedWfs());
        typeDto.getWorkflowTypes().add(workflowRepository.getRejectedWfs());

        return typeDto;
    }

    @Transactional(readOnly = true)
    public WorkflowTypesDto getObjectTypeWorkflows() {
        WorkflowTypesDto typeDto = new WorkflowTypesDto();
        typeDto.getWorkflowObjectTypes().add(workflowRepository.getTypeWorkflows(PLMObjectType.ITEMREVISION));
        typeDto.getWorkflowObjectTypes().add(workflowRepository.getTypeWorkflows(PLMObjectType.CHANGE));
        typeDto.getWorkflowObjectTypes().add(workflowRepository.getTypeWorkflows(PLMObjectType.QUALITY));
        typeDto.getWorkflowObjectTypes().add(workflowRepository.getTypeWorkflows(PLMObjectType.MANUFACTURER));
        typeDto.getWorkflowObjectTypes().add(workflowRepository.getTypeWorkflows(PLMObjectType.MANUFACTURERPART));
        typeDto.getWorkflowObjectTypes().add(workflowRepository.getTypeWorkflows(PLMObjectType.PLMNPR));
        typeDto.getWorkflowObjectTypes().add(workflowRepository.getTypeWorkflows(PLMObjectType.valueOf(MROEnumObject.MROWORKORDER.toString())));
        typeDto.getWorkflowObjectTypes().add(workflowRepository.getTypeWorkflows(PLMObjectType.PROJECT));
        typeDto.getWorkflowObjectTypes().add(workflowRepository.getTypeWorkflows(PLMObjectType.PROJECTACTIVITY));
        Integer reqs = workflowRepository.getTypeWorkflows(PLMObjectType.REQUIREMENT);
        Integer spec = workflowRepository.getTypeWorkflows(PLMObjectType.SPECIFICATION);
        typeDto.getWorkflowObjectTypes().add(reqs + spec);
        return typeDto;
    }

    @Transactional(readOnly = true)
    public WorkflowTypesDto getChangeTypeWorkflows() {
        WorkflowTypesDto typeDto = new WorkflowTypesDto();
        typeDto.getWorkflowObjectTypes().add(workflowRepository.getTypeWorkflows(PLMObjectType.CHANGE));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflow> getAllStartedWorkflows() {
        return workflowRepository.findByStartedWorkflows();
    }

    @Transactional(readOnly = true)
    public WorkflowCountsDto getTypeByCounts() {
        WorkflowCountsDto typeDto = new WorkflowCountsDto();
        typeDto.setItems(workflowRepository.getTypeWorkflows(PLMObjectType.ITEMREVISION));
        Integer changeTypes = workflowRepository.getTypeWorkflows(PLMObjectType.CHANGE);
        Integer itemMcoTypes = workflowRepository.getTypeWorkflows(PLMObjectType.ITEMMCO);
        Integer partMcoTypes = workflowRepository.getTypeWorkflows(PLMObjectType.OEMPARTMCO);
        typeDto.setChanges(changeTypes + itemMcoTypes + partMcoTypes);
        Integer qcrType = workflowRepository.getTypeWorkflows(PLMObjectType.QCR);
        Integer ncrType = workflowRepository.getTypeWorkflows(PLMObjectType.NCR);
        Integer prType = workflowRepository.getTypeWorkflows(PLMObjectType.PROBLEMREPORT);
        Integer itemInspectionType = workflowRepository.getTypeWorkflows(PLMObjectType.ITEMINSPECTION);
        Integer matInspectionType = workflowRepository.getTypeWorkflows(PLMObjectType.MATERIALINSPECTION);
        Integer productInspectionType = workflowRepository.getTypeWorkflows(PLMObjectType.INSPECTIONPLANREVISION);
        typeDto.setQuality(qcrType + ncrType + prType + itemInspectionType + matInspectionType + productInspectionType);
        typeDto.setMfr(workflowRepository.getTypeWorkflows(PLMObjectType.MANUFACTURER));
        typeDto.setMfrParts(workflowRepository.getTypeWorkflows(PLMObjectType.MANUFACTURERPART));
        typeDto.setNpr(workflowRepository.getTypeWorkflows(PLMObjectType.PLMNPR));
        typeDto.setWorkOrder(workflowRepository.getTypeWorkflows(PLMObjectType.valueOf(MROEnumObject.MROWORKORDER.toString())));
        Integer project = workflowRepository.getTypeWorkflows(PLMObjectType.PROJECT);
        Integer activity = workflowRepository.getTypeWorkflows(PLMObjectType.PROJECTACTIVITY);
        typeDto.setProjects(project + activity);
        Integer reqs = workflowRepository.getTypeWorkflows(PLMObjectType.REQUIREMENT);
        Integer spec = workflowRepository.getTypeWorkflows(PLMObjectType.SPECIFICATION);
        typeDto.setRequirements(reqs + spec);
        return typeDto;
    }

}
