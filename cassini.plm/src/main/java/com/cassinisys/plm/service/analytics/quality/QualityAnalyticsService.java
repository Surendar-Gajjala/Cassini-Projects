package com.cassinisys.plm.service.analytics.quality;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.plm.model.analytics.quality.*;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMMfrPartInspectionReport;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.mfr.MfrPartInspectionReportRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pqm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by subramanyam on 17-07-2020.
 */
@Service
public class QualityAnalyticsService {

    @Autowired
    private InspectionPlanRepository inspectionPlanRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private ProblemReportTypeRepository problemReportTypeRepository;
    @Autowired
    private NCRTypeRepository ncrTypeRepository;
    @Autowired
    private ItemInspectionRepository itemInspectionRepository;
    @Autowired
    private MaterialInspectionRepository materialInspectionRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PQMCustomerRepository pqmCustomerRepository;
    @Autowired
    private NCRProblemItemRepository ncrProblemItemRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;
    @Autowired
    private PPAPRepository ppapRepository;
    @Autowired
    private PQMSupplierAuditPlanRepository supplierAuditPlanRepository;
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private PPAPChecklistRepository ppapChecklistRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private DocumentReviewerRepository documentReviewerRepository;

    @Transactional(readOnly = true)
    public QualityByTypeDto getQualitiesByType() {
        QualityByTypeDto typeDto = new QualityByTypeDto();

        typeDto.getQualityTypes().add(problemReportRepository.getTotalPrs());
        typeDto.getQualityTypes().add(ncrRepository.getTotalNcrs());
        typeDto.getQualityTypes().add(qcrRepository.getTotalQcrs());
        return typeDto;
    }

    @Transactional(readOnly = true)
    public QualityByTypeDto getStaticDashboardCounts() {
        QualityByTypeDto typeDto = new QualityByTypeDto();

        typeDto.getQualityTypes().add(problemReportRepository.getTotalPrs());
        typeDto.getQualityTypes().add(ncrRepository.getTotalNcrs());
        typeDto.getQualityTypes().add(qcrRepository.getTotalQcrs());

        typeDto.getInspectionPlansByStatus().add(inspectionPlanRevisionRepository.getPendingInspectionPlans());
        typeDto.getInspectionPlansByStatus().add(inspectionPlanRevisionRepository.getReleasedInspectionsPlans());
        typeDto.getInspectionPlansByStatus().add(inspectionPlanRevisionRepository.getRejectedInspectionsPlans());

        typeDto.getInspectionsByStatus().add(inspectionRepository.getPendingInspections());
        typeDto.getInspectionsByStatus().add(inspectionRepository.getReleasedInspections());
        typeDto.getInspectionsByStatus().add(inspectionRepository.getRejectedInspections(WorkflowStatusType.REJECTED));

        typeDto.getPrsByStatus().add(problemReportRepository.getPendingPrs());
        typeDto.getPrsByStatus().add(problemReportRepository.getApprovedPrs());
        typeDto.getPrsByStatus().add(problemReportRepository.getRejectedPrs(WorkflowStatusType.REJECTED));

        typeDto.getPrsBySource().add(problemReportRepository.getPrsByReporterType(ReporterType.CUSTOMER));
        typeDto.getPrsBySource().add(problemReportRepository.getPrsByReporterType(ReporterType.INTERNAL));
        typeDto.getPrsBySource().add(problemReportRepository.getPrsByReporterType(ReporterType.SUPPLIER));

        typeDto.getPpapByStatus().add(ppapRepository.getPpapCountsByStatus(LifeCyclePhaseType.PRELIMINARY));
        typeDto.getPpapByStatus().add(ppapRepository.getPpapCountsByStatus(LifeCyclePhaseType.REVIEW));
        typeDto.getPpapByStatus().add(ppapRepository.getPpapCountsByStatus(LifeCyclePhaseType.RELEASED));

        typeDto.getNcrsByStatus().add(ncrRepository.getPendingNcrs());
        typeDto.getNcrsByStatus().add(ncrRepository.getApprovedNcrs());
        typeDto.getNcrsByStatus().add(ncrRepository.getRejectedNcrs(WorkflowStatusType.REJECTED));

        typeDto.getQcrsByStatus().add(qcrRepository.getPendingQcrs());
        typeDto.getQcrsByStatus().add(qcrRepository.getApprovedQcrs());
        typeDto.getQcrsByStatus().add(qcrRepository.getRejectedQcrs(WorkflowStatusType.REJECTED));

        typeDto.getQcrsByType().add(qcrRepository.getQcrsByFor(QCRFor.PR));
        typeDto.getQcrsByType().add(qcrRepository.getQcrsByFor(QCRFor.NCR));

        typeDto.getSupplierAuditsByStatusCounts().add(supplierAuditPlanRepository.getSupplierAuditStatusCounts(AuditPlanStatus.NONE));
        typeDto.getSupplierAuditsByStatusCounts().add(supplierAuditPlanRepository.getSupplierAuditStatusCounts(AuditPlanStatus.PLANNED));
        typeDto.getSupplierAuditsByStatusCounts().add(supplierAuditPlanRepository.getSupplierAuditStatusCounts(AuditPlanStatus.COMPLETED));
        typeDto.getSupplierAuditsByStatusCounts().add(supplierAuditPlanRepository.getSupplierAuditStatusCounts(AuditPlanStatus.APPROVED));
        Integer overDueCount = 0;
        List<PQMSupplierAudit> supplierAudits = supplierAuditRepository.findAll();
        for (PQMSupplierAudit supplierAudit : supplierAudits) {

            Integer overDueNo = supplierAuditPlanRepository.getOverdueSupplerAudits(supplierAudit.getId(), new Date());
            if (overDueNo > 0) {
                overDueCount += 1;

            }
        }
        typeDto.getSupplierAuditsByStatusCounts().add(overDueCount);

        return typeDto;
    }

    @Transactional
    public QualityCardCounts getQualityDashboardCounts() {
        QualityCardCounts cardCounts = new QualityCardCounts();

        cardCounts.setInspectionPlans(inspectionPlanRepository.getAllInspectionPlanCounts());
        cardCounts.setApprovedInspectionPlans(inspectionPlanRevisionRepository.getReleasedInspectionsPlans());
        cardCounts.setInspections(inspectionRepository.getAllInspectionCounts());
        cardCounts.setApprovedInspections(inspectionRepository.getReleasedInspections());

        cardCounts.setProblemReports(problemReportRepository.getTotalPrs());
        cardCounts.setImplementedPrs(problemReportRepository.getImplementedPrsCounts());
        cardCounts.setNcrs(ncrRepository.getTotalNcrs());
        cardCounts.setImplementedNcrs(ncrRepository.getImplementedNcrsCounts());
        cardCounts.setQcrs(qcrRepository.getTotalQcrs());
        cardCounts.setImplementedQcrs(qcrRepository.getImplementedQcrsCounts());
        cardCounts.setPpap(ppapRepository.getAllPpapCounts());
        cardCounts.setApprovedPpap(ppapRepository.getPpapCountsByStatus(LifeCyclePhaseType.RELEASED));
        cardCounts.setSupplierAudits(supplierAuditRepository.getTotalSupplierAudit());
        cardCounts.setApprovedSupplierAudits(supplierAuditRepository.getSupplierAuditCounts(LifeCyclePhaseType.RELEASED));


        return cardCounts;
    }

    @Transactional(readOnly = true)
    public QualityByTypeDto getInspectionPlansByStatus() {
        QualityByTypeDto typeDto = new QualityByTypeDto();
        typeDto.getInspectionPlansByStatus().add(inspectionPlanRevisionRepository.getPendingInspectionPlans());
        typeDto.getInspectionPlansByStatus().add(inspectionPlanRevisionRepository.getReleasedInspectionsPlans());
        typeDto.getInspectionPlansByStatus().add(inspectionPlanRevisionRepository.getRejectedInspectionsPlans());
        return typeDto;
    }

    @Transactional(readOnly = true)
    public QualityByTypeDto getInspectionsByStatus() {
        QualityByTypeDto typeDto = new QualityByTypeDto();
        typeDto.getInspectionsByStatus().add(inspectionRepository.getPendingInspections());
        typeDto.getInspectionsByStatus().add(inspectionRepository.getReleasedInspections());
        typeDto.getInspectionsByStatus().add(inspectionRepository.getRejectedInspections(WorkflowStatusType.REJECTED));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public QualityByTypeDto getProblemReportsByStatus() {
        QualityByTypeDto typeDto = new QualityByTypeDto();
        typeDto.getPrsByStatus().add(problemReportRepository.getPendingPrs());
        typeDto.getPrsByStatus().add(problemReportRepository.getApprovedPrs());
        typeDto.getPrsByStatus().add(problemReportRepository.getRejectedPrs(WorkflowStatusType.REJECTED));

        return typeDto;
    }

    @Transactional(readOnly = true)
    public QualityByTypeDto getProblemReportsBySource() {
        QualityByTypeDto typeDto = new QualityByTypeDto();
        typeDto.getPrsBySource().add(problemReportRepository.getPrsByReporterType(ReporterType.CUSTOMER));
        typeDto.getPrsBySource().add(problemReportRepository.getPrsByReporterType(ReporterType.INTERNAL));
        typeDto.getPrsBySource().add(problemReportRepository.getPrsByReporterType(ReporterType.SUPPLIER));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public QualityByTypeDto getPpapByStatus() {
        QualityByTypeDto typeDto = new QualityByTypeDto();
        typeDto.getPpapByStatus().add(ppapRepository.getPpapCountsByStatus(LifeCyclePhaseType.PRELIMINARY));
        typeDto.getPpapByStatus().add(ppapRepository.getPpapCountsByStatus(LifeCyclePhaseType.REVIEW));
        typeDto.getPpapByStatus().add(ppapRepository.getPpapCountsByStatus(LifeCyclePhaseType.RELEASED));

        return typeDto;
    }

    @Transactional(readOnly = true)
    public QualityByTypeDto getSupplierAuditByStatus() {
        QualityByTypeDto typeDto = new QualityByTypeDto();
        typeDto.getSupplierAuditsByStatusCounts().add(supplierAuditPlanRepository.getSupplierAuditStatusCounts(AuditPlanStatus.NONE));
        typeDto.getSupplierAuditsByStatusCounts().add(supplierAuditPlanRepository.getSupplierAuditStatusCounts(AuditPlanStatus.PLANNED));
        typeDto.getSupplierAuditsByStatusCounts().add(supplierAuditPlanRepository.getSupplierAuditStatusCounts(AuditPlanStatus.COMPLETED));
        typeDto.getSupplierAuditsByStatusCounts().add(supplierAuditPlanRepository.getSupplierAuditStatusCounts(AuditPlanStatus.APPROVED));
        Integer overDueCount = 0;
        List<PQMSupplierAudit> supplierAudits = supplierAuditRepository.findAll();
        for (PQMSupplierAudit supplierAudit : supplierAudits) {

            Integer overDueNo = supplierAuditPlanRepository.getOverdueSupplerAudits(supplierAudit.getId(), new Date());
            if (overDueNo > 0) {
                overDueCount += 1;

            }
        }
        typeDto.getSupplierAuditsByStatusCounts().add(overDueCount);


        return typeDto;
    }

    @Transactional(readOnly = true)
    public ProblemReportCounts getProblemReportsBySeverity() {
        ProblemReportCounts problemReportCounts = new ProblemReportCounts();
        List<String> prSeverities = new ArrayList<>();
        List<Lov> severityLovs = problemReportTypeRepository.getUniqueProblemReportSeverities();
        for (Lov lov : severityLovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (prSeverities.indexOf(lov.getValues()[i]) == -1) {
                    prSeverities.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.getPrSeverities().addAll(prSeverities);
        prSeverities.forEach(severity -> {
            problemReportCounts.getPrSeverityCounts().add(problemReportRepository.getPrsBySeverity(severity));
        });

        return problemReportCounts;
    }

    @Transactional(readOnly = true)
    public ProblemReportCounts getProblemReportsByFailure() {
        ProblemReportCounts typeDto = new ProblemReportCounts();
        List<String> failures = new ArrayList<>();
        List<Lov> lovs = problemReportTypeRepository.getUniqueProblemReportFailureTypes();
        for (Lov lov : lovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (failures.indexOf(lov.getValues()[i]) == -1) {
                    failures.add(lov.getValues()[i]);
                }
            }
        }
        typeDto.setPrFailures(failures);
        failures.forEach(failure -> {
            typeDto.getPrFailureCounts().add(problemReportRepository.getPrsByFailureType(failure));
        });

        return typeDto;
    }

    @Transactional(readOnly = true)
    public ProblemReportCounts getProblemReportsByDisposition() {
        ProblemReportCounts problemReportCounts = new ProblemReportCounts();
        List<String> prDispositions = new ArrayList<>();
        List<Lov> dispositionLovs = problemReportTypeRepository.getUniqueProblemReportDispositions();
        for (Lov lov : dispositionLovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (prDispositions.indexOf(lov.getValues()[i]) == -1) {
                    prDispositions.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.setPrDispositions(prDispositions);
        prDispositions.forEach(disposition -> {
            problemReportCounts.getPrDispositionCounts().add(problemReportRepository.getPrsByDisposition(disposition));
        });

        return problemReportCounts;
    }


    @Transactional(readOnly = true)
    public ProblemReportCounts getNcrsBySeverity() {
        ProblemReportCounts problemReportCounts = new ProblemReportCounts();
        List<String> severities = new LinkedList<>();
        List<Lov> ncrSeverityLovs = ncrTypeRepository.getUniqueNcrSeverities();
        for (Lov lov : ncrSeverityLovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (severities.indexOf(lov.getValues()[i]) == -1) {
                    severities.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.getNcrSeverities().addAll(severities);
        severities.forEach(severity -> {
            problemReportCounts.getNcrSeverityCounts().add(ncrRepository.getNcrsBySeverity(severity));
        });

        return problemReportCounts;
    }

    @Transactional(readOnly = true)
    public ProblemReportCounts getNcrsByFailure() {
        ProblemReportCounts problemReportCounts = new ProblemReportCounts();
        List<String> failures = new LinkedList<>();
        List<Lov> ncrFailureLovs = ncrTypeRepository.getUniqueNcrFailureTypes();
        for (Lov lov : ncrFailureLovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (failures.indexOf(lov.getValues()[i]) == -1) {
                    failures.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.getNcrFailures().addAll(failures);
        failures.forEach(failure -> {
            problemReportCounts.getNcrFailureCounts().add(ncrRepository.getNcrsByFailureType(failure));
        });

        return problemReportCounts;
    }

    @Transactional(readOnly = true)
    public ProblemReportCounts getNcrsByDisposition() {
        ProblemReportCounts problemReportCounts = new ProblemReportCounts();
        List<String> dispositions = new LinkedList<>();
        List<Lov> ncrDispositionLovs = ncrTypeRepository.getUniqueNcrDispositions();
        for (Lov lov : ncrDispositionLovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (dispositions.indexOf(lov.getValues()[i]) == -1) {
                    dispositions.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.getNcrDispositions().addAll(dispositions);
        dispositions.forEach(disposition -> {
            problemReportCounts.getNcrDispositionCounts().add(ncrRepository.getNcrsByDisposition(disposition));
        });

        return problemReportCounts;
    }

    @Transactional(readOnly = true)
    public ProblemReportCounts getObjectSeverityFailureDispositions() {
        ProblemReportCounts problemReportCounts = new ProblemReportCounts();

        List<String> severities = new LinkedList<>();
        List<Lov> ncrSeverityLovs = ncrTypeRepository.getUniqueNcrSeverities();
        for (Lov lov : ncrSeverityLovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (severities.indexOf(lov.getValues()[i]) == -1) {
                    severities.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.getNcrSeverities().addAll(severities);
        severities.forEach(severity -> {
            problemReportCounts.getNcrSeverityCounts().add(ncrRepository.getNcrsBySeverity(severity));
        });

        List<String> failures = new LinkedList<>();
        List<Lov> ncrFailureLovs = ncrTypeRepository.getUniqueNcrFailureTypes();
        for (Lov lov : ncrFailureLovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (failures.indexOf(lov.getValues()[i]) == -1) {
                    failures.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.getNcrFailures().addAll(failures);
        failures.forEach(failure -> {
            problemReportCounts.getNcrFailureCounts().add(ncrRepository.getNcrsByFailureType(failure));
        });

        List<String> dispositions = new LinkedList<>();
        List<Lov> ncrDispositionLovs = ncrTypeRepository.getUniqueNcrDispositions();
        for (Lov lov : ncrDispositionLovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (dispositions.indexOf(lov.getValues()[i]) == -1) {
                    dispositions.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.getNcrDispositions().addAll(dispositions);
        dispositions.forEach(disposition -> {
            problemReportCounts.getNcrDispositionCounts().add(ncrRepository.getNcrsByDisposition(disposition));
        });

        List<String> prSeverities = new ArrayList<>();
        List<Lov> severityLovs = problemReportTypeRepository.getUniqueProblemReportSeverities();
        for (Lov lov : severityLovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (prSeverities.indexOf(lov.getValues()[i]) == -1) {
                    prSeverities.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.getPrSeverities().addAll(prSeverities);
        prSeverities.forEach(severity -> {
            problemReportCounts.getPrSeverityCounts().add(problemReportRepository.getPrsBySeverity(severity));
        });

        List<String> prFailures = new ArrayList<>();
        List<Lov> lovs = problemReportTypeRepository.getUniqueProblemReportFailureTypes();
        for (Lov lov : lovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (prFailures.indexOf(lov.getValues()[i]) == -1) {
                    prFailures.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.setPrFailures(prFailures);
        prFailures.forEach(failure -> {
            problemReportCounts.getPrFailureCounts().add(problemReportRepository.getPrsByFailureType(failure));
        });

        List<String> prDispositions = new ArrayList<>();
        List<Lov> dispositionLovs = problemReportTypeRepository.getUniqueProblemReportDispositions();
        for (Lov lov : dispositionLovs) {
            for (int i = 0; i < lov.getValues().length; i++) {
                if (prDispositions.indexOf(lov.getValues()[i]) == -1) {
                    prDispositions.add(lov.getValues()[i]);
                }
            }
        }
        problemReportCounts.setPrDispositions(prDispositions);
        prDispositions.forEach(disposition -> {
            problemReportCounts.getPrDispositionCounts().add(problemReportRepository.getPrsByDisposition(disposition));
        });

        return problemReportCounts;
    }

    @Transactional(readOnly = true)
    public QualityByTypeDto getNcrsByStatus() {
        QualityByTypeDto typeDto = new QualityByTypeDto();
        typeDto.getNcrsByStatus().add(ncrRepository.getPendingNcrs());
        typeDto.getNcrsByStatus().add(ncrRepository.getApprovedNcrs());
        typeDto.getNcrsByStatus().add(ncrRepository.getRejectedNcrs(WorkflowStatusType.REJECTED));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public QualityByTypeDto getQcrsByStatus() {
        QualityByTypeDto typeDto = new QualityByTypeDto();
        typeDto.getQcrsByStatus().add(qcrRepository.getPendingQcrs());
        typeDto.getQcrsByStatus().add(qcrRepository.getApprovedQcrs());
        typeDto.getQcrsByStatus().add(qcrRepository.getRejectedQcrs(WorkflowStatusType.REJECTED));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public QualityByTypeDto getQcrsByType() {
        QualityByTypeDto typeDto = new QualityByTypeDto();
        typeDto.getQcrsByType().add(qcrRepository.getQcrsByFor(QCRFor.PR));
        typeDto.getQcrsByType().add(qcrRepository.getQcrsByFor(QCRFor.NCR));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public List<InspectionFailureProducts> getTopInspectionFailureProducts() {
        List<InspectionFailureProducts> failureProducts = new ArrayList<>();

        List<Object[]> inspectionCounts = itemInspectionRepository.getInspectionFailedProducts();

        for (Object[] row : inspectionCounts) {
            Integer itemId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            InspectionFailureProducts inspectionFailureProduct = new InspectionFailureProducts();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            inspectionFailureProduct.setProductId(itemRevision.getId());
            inspectionFailureProduct.setItemName(item.getItemName());
            inspectionFailureProduct.setItemNumber(item.getItemNumber());
            inspectionFailureProduct.setCount(count);
            failureProducts.add(inspectionFailureProduct);
        }

        return failureProducts;
    }

    @Transactional(readOnly = true)
    public List<InspectionFailureMaterials> getTopInspectionFailureMaterials() {
        List<InspectionFailureMaterials> failureMaterials = new ArrayList<>();

        List<Object[]> inspectionCounts = materialInspectionRepository.getInspectionFailedMaterials();

        for (Object[] row : inspectionCounts) {
            Integer itemId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            InspectionFailureMaterials inspectionFailureMaterial = new InspectionFailureMaterials();
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(itemId);
            inspectionFailureMaterial.setMfrId(manufacturerPart.getManufacturer());
            inspectionFailureMaterial.setMfrPartId(manufacturerPart.getId());
            inspectionFailureMaterial.setPartName(manufacturerPart.getPartName());
            inspectionFailureMaterial.setPartNumber(manufacturerPart.getPartNumber());
            inspectionFailureMaterial.setCount(count);
            failureMaterials.add(inspectionFailureMaterial);
        }

        return failureMaterials;
    }

    @Transactional(readOnly = true)
    public List<ProductProblems> getTopProductProblems() {
        List<ProductProblems> productProblems = new ArrayList<>();
        List<Object[]> productCountDto = problemReportRepository.getProductProblems();
        for (Object[] row : productCountDto) {
            Integer itemId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            ProductProblems productProblem = new ProductProblems();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            productProblem.setProductId(itemRevision.getId());
            productProblem.setRevision(itemRevision.getRevision());
            productProblem.setItemName(item.getItemName());
            productProblem.setItemNumber(item.getItemNumber());
            productProblem.setCount(count);

            productProblems.add(productProblem);
        }

        return productProblems;
    }

    @Transactional(readOnly = true)
    public List<CustomerReportingProblems> getTopCustomerReportingProblems() {
        List<CustomerReportingProblems> customerReportingProblems = new ArrayList<>();
        List<Object[]> customerCountDto = problemReportRepository.getCustomerReportingProblems();

        for (Object[] row : customerCountDto) {
            Integer reportedBy = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            CustomerReportingProblems customerReportingProblem = new CustomerReportingProblems();
            if (reportedBy != null) {
                PQMCustomer customer = pqmCustomerRepository.getQualityCustomer(reportedBy);
                customerReportingProblem.setCustomerId(customer.getId());
                customerReportingProblem.setCount(count);
                customerReportingProblem.setCustomerName(customer.getName());
            }
            customerReportingProblems.add(customerReportingProblem);
        }

        return customerReportingProblems;
    }

    @Transactional(readOnly = true)
    public List<ManufacturersForNCR> getTopManufacturersForNCR() {
        List<ManufacturersForNCR> manufacturersForNCRs = new ArrayList<>();
        List<Object[]> ncrMfrCountDto = ncrProblemItemRepository.getManufacturersForNCRCounts();

        for (Object[] row : ncrMfrCountDto) {
            Integer manufacturerId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            ManufacturersForNCR manufacturersForNCR = new ManufacturersForNCR();
            PLMManufacturer manufacturer = manufacturerRepository.findOne(manufacturerId);
            manufacturersForNCR.setMfrId(manufacturer.getId());
            manufacturersForNCR.setManufacturer(manufacturer.getName());
            manufacturersForNCR.setCount(count);

            manufacturersForNCRs.add(manufacturersForNCR);
        }
        return manufacturersForNCRs;
    }

    @Transactional
    public InspectionReportDto getMfrInspectionReportCounts() {
        InspectionReportDto inspectionReportDto = new InspectionReportDto();

        inspectionReportDto.getMonths().add("Jan");
        inspectionReportDto.getMonths().add("Feb");
        inspectionReportDto.getMonths().add("Mar");
        inspectionReportDto.getMonths().add("Apr");
        inspectionReportDto.getMonths().add("May");
        inspectionReportDto.getMonths().add("Jun");
        inspectionReportDto.getMonths().add("Jul");
        inspectionReportDto.getMonths().add("Aug");
        inspectionReportDto.getMonths().add("Sept");
        inspectionReportDto.getMonths().add("Oct");
        inspectionReportDto.getMonths().add("Nov");
        inspectionReportDto.getMonths().add("Dec");

        Map<String, Map<Integer, Integer>> monthReportMap = new LinkedHashMap<>();

        List<Integer> lifecycleIds = mfrPartInspectionReportRepository.getInspectionReportLifecycleIds();
        if (lifecycleIds.size() > 0) {
            List<PLMLifeCycle> lifeCycles = lifeCycleRepository.findByIdIn(lifecycleIds);
            lifeCycles.forEach(plmLifeCycle -> {
                plmLifeCycle.getPhases().forEach(plmLifeCyclePhase -> {
                    monthReportMap.put(plmLifeCyclePhase.getPhase(), setMonthMap());
                });
            });

            monthReportMap.put("Approved", setMonthMap());
            monthReportMap.put("Rejected", setMonthMap());
        }

        List<PLMMfrPartInspectionReport> mfrPartInspectionReports = mfrPartInspectionReportRepository.findByLatestTrueAndFileTypeOrderByLifeCyclePhaseIdAsc("FILE");
        for (PLMMfrPartInspectionReport mfrPartInspectionReport : mfrPartInspectionReports) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(mfrPartInspectionReport.getCreatedDate());

            Integer month = cal.get(Calendar.MONTH);

            Map<Integer, Integer> integerMap = monthReportMap.get(mfrPartInspectionReport.getLifeCyclePhase().getPhase());
            if (integerMap == null) {
                integerMap = setMonthMap();
            }
            Integer approvedCount = documentReviewerRepository.getApprovedCount(mfrPartInspectionReport.getId());
            Integer rejectedCount = documentReviewerRepository.getRejectedCount(mfrPartInspectionReport.getId());
            if (approvedCount > 0) {
                Map<Integer, Integer> approvedMap = monthReportMap.get("Approved");
                if (approvedMap == null) {
                    approvedMap = setMonthMap();
                }
                Integer count = approvedMap.containsKey(month) ? approvedMap.get(month) : 0;

                count = count + 1;
                approvedMap.put(month, count);
                monthReportMap.put("Approved", approvedMap);
            } else if (rejectedCount > 0) {
                Map<Integer, Integer> rejectedMap = monthReportMap.get("Rejected");
                if (rejectedMap == null) {
                    rejectedMap = setMonthMap();
                }
                Integer count = rejectedMap.containsKey(month) ? rejectedMap.get(month) : 0;

                count = count + 1;
                rejectedMap.put(month, count);
                monthReportMap.put("Rejected", rejectedMap);
            }
            Integer fileCount = integerMap.containsKey(month) ? integerMap.get(month) : 0;

            fileCount = fileCount + 1;
            integerMap.put(month, fileCount);
            monthReportMap.put(mfrPartInspectionReport.getLifeCyclePhase().getPhase(), integerMap);
        }

        for (String key : monthReportMap.keySet()) {
            Map<Integer, Integer> monthMap = monthReportMap.get(key);
            MonthInspectionReportDto monthInspectionReportDto = new MonthInspectionReportDto();
            monthInspectionReportDto.setName(key);
            monthInspectionReportDto.setData(new LinkedList<>(monthMap.values()));
            inspectionReportDto.getMonthReports().add(monthInspectionReportDto);
        }

        return inspectionReportDto;
    }

    private LinkedHashMap<Integer, Integer> setMonthMap() {
        LinkedHashMap<Integer, Integer> integerMap = new LinkedHashMap<>();
        integerMap.put(0, 0);
        integerMap.put(1, 0);
        integerMap.put(2, 0);
        integerMap.put(3, 0);
        integerMap.put(4, 0);
        integerMap.put(5, 0);
        integerMap.put(6, 0);
        integerMap.put(7, 0);
        integerMap.put(8, 0);
        integerMap.put(9, 0);
        integerMap.put(10, 0);
        integerMap.put(11, 0);
        return integerMap;
    }

    @Transactional(readOnly = true)
    public PPAPChecklistStatusDto getPPAPChecklistStatus() {
        PPAPChecklistStatusDto typeDto = new PPAPChecklistStatusDto();
        Lov lov = lovRepository.findByName("Default PPAP Checklist");
        PLMLifeCycle lifeCycle = lifeCycleRepository.findByName("Default PPAP Checklist Lifecycle");
        lifeCycle.setPhases(lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId()));
        Map<String, Map<String, Integer>> ppapCheckListStatusMap = new LinkedHashMap<>();
        for (String val : lov.getValues()) {
            typeDto.getPpapFolders().add(val);
            for (PLMLifeCyclePhase phase : lifeCycle.getPhases()) {
                if (!phase.getPhase().equals("None")) {
                    Map<String, Integer> phaseCountMap = ppapCheckListStatusMap.containsKey(phase.getPhase()) ? ppapCheckListStatusMap.get(phase.getPhase()) : new LinkedHashMap<>();
                    Integer count = ppapChecklistRepository.getChecklistIdsByName(val, phase.getPhase());
                    Integer phaseCount = phaseCountMap.containsKey(val) ? phaseCountMap.get(val) : 0;
                    phaseCount = phaseCount + count;
                    phaseCountMap.put(val, phaseCount);
                    ppapCheckListStatusMap.put(phase.getPhase(), phaseCountMap);
                }
            }
        }

        for (String key : ppapCheckListStatusMap.keySet()) {
            Map<String, Integer> monthMap = ppapCheckListStatusMap.get(key);
            PPAPFolderReportDto ppapFolderReportDto = new PPAPFolderReportDto();
            ppapFolderReportDto.setName(key);
            ppapFolderReportDto.setData(new LinkedList<>(monthMap.values()));
            typeDto.getFoldersReports().add(ppapFolderReportDto);
        }
        return typeDto;
    }

    @Transactional
    public SupplierAuditReportDto getSupplierAuditReportCounts() {
        SupplierAuditReportDto supplierAuditReportDto = new SupplierAuditReportDto();

        List<String> years = supplierAuditRepository.getUniquePlannedYears();

        supplierAuditReportDto.setYears(years);

        Map<String, Map<String, Integer>> yearReportMap = new LinkedHashMap<>();

        PLMLifeCycle lifeCycle = lifeCycleRepository.findByName("Default Supplier Audit Lifecycle");

        for (String year : years) {

            for (PLMLifeCyclePhase lifeCyclePhase : lifeCycle.getPhases()) {

                Map<String, Integer> integerMap = yearReportMap.containsKey(lifeCyclePhase.getPhase()) ? yearReportMap.get(lifeCyclePhase.getPhase()) : new LinkedHashMap<>();

                Integer counts = supplierAuditRepository.getSupplierAuditCountsByYearAndStatus(lifeCyclePhase.getPhase(), year);

                Integer yearCounts = integerMap.containsKey(year) ? integerMap.get(year) : 0;

                yearCounts = yearCounts + counts;
                integerMap.put(year, yearCounts);
                yearReportMap.put(lifeCyclePhase.getPhase(), integerMap);

            }
        }

        for (String key : yearReportMap.keySet()) {
            Map<String, Integer> monthMap = yearReportMap.get(key);
            MonthInspectionReportDto monthInspectionReportDto = new MonthInspectionReportDto();
            monthInspectionReportDto.setName(key);
            monthInspectionReportDto.setData(new LinkedList<>(monthMap.values()));
            supplierAuditReportDto.getYearReports().add(monthInspectionReportDto);
        }

        return supplierAuditReportDto;
    }
}
