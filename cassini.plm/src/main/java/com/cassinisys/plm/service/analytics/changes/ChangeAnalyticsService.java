package com.cassinisys.plm.service.analytics.changes;

import com.cassinisys.plm.model.analytics.changes.ChangeCardCounts;
import com.cassinisys.plm.model.analytics.changes.ChangeTypesDto;
import com.cassinisys.plm.model.cm.VarianceType;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.cassinisys.plm.repo.cm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by subramanyam on 17-07-2020.
 */
@Service
public class ChangeAnalyticsService {

    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private DCRRepository dcrRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private VarianceRepository varianceRepository;

    @Transactional(readOnly = true)
    public ChangeTypesDto getChangeDashboardCounts() {
        ChangeTypesDto typeDto = new ChangeTypesDto();

        typeDto.getEcrCounts().add(ecrRepository.getNotStartedEcrs());
        typeDto.getEcrCounts().add(ecrRepository.getPendingEcrs(WorkflowStatusType.NORMAL, WorkflowStatusType.UNDEFINED));
        typeDto.getEcrCounts().add(ecrRepository.getReleasedEcrs());
        typeDto.getEcrCounts().add(ecrRepository.getRejectedEcrs(WorkflowStatusType.REJECTED));
        typeDto.getEcrCounts().add(ecrRepository.getOnHoldEcrs());

        typeDto.getEcoCounts().add(ecoRepository.getNotStartedEcos());
        typeDto.getEcoCounts().add(ecoRepository.getPendingEcos(WorkflowStatusType.NORMAL, WorkflowStatusType.UNDEFINED));
        typeDto.getEcoCounts().add(ecoRepository.getReleasedEcos());
        typeDto.getEcoCounts().add(ecoRepository.getRejectedEcos(WorkflowStatusType.REJECTED));
        typeDto.getEcoCounts().add(ecoRepository.getOnHoldEcos());

        typeDto.getDcrCounts().add(dcrRepository.getNotStartedDcrs());
        typeDto.getDcrCounts().add(dcrRepository.getPendingDcrs(WorkflowStatusType.NORMAL, WorkflowStatusType.UNDEFINED));
        typeDto.getDcrCounts().add(dcrRepository.getReleasedDcrs());
        typeDto.getDcrCounts().add(dcrRepository.getRejectedDcrs(WorkflowStatusType.REJECTED));
        typeDto.getDcrCounts().add(dcrRepository.getOnHoldDcrs());

        typeDto.getDcoCounts().add(dcoRepository.getNotStartedDcos());
        typeDto.getDcoCounts().add(dcoRepository.getPendingDcos(WorkflowStatusType.NORMAL, WorkflowStatusType.UNDEFINED));
        typeDto.getDcoCounts().add(dcoRepository.getReleasedDcos());
        typeDto.getDcoCounts().add(dcoRepository.getRejectedDcos(WorkflowStatusType.REJECTED));
        typeDto.getDcoCounts().add(dcoRepository.getOnHoldDcos());

        typeDto.getMcoCounts().add(mcoRepository.getNotStartedMcos());
        typeDto.getMcoCounts().add(mcoRepository.getPendingDcos(WorkflowStatusType.NORMAL, WorkflowStatusType.UNDEFINED));
        typeDto.getMcoCounts().add(mcoRepository.getReleasedDcos());
        typeDto.getMcoCounts().add(mcoRepository.getRejectedDcos(WorkflowStatusType.REJECTED));
        typeDto.getMcoCounts().add(mcoRepository.getOnHoldMcos());

        typeDto.getDeviationCounts().add(varianceRepository.getNotStartedByVarianceType(VarianceType.DEVIATION));
        typeDto.getDeviationCounts().add(varianceRepository.getVariancesByTypeAndStatusAndStarted(VarianceType.DEVIATION, WorkflowStatusType.NORMAL, WorkflowStatusType.UNDEFINED));
        typeDto.getDeviationCounts().add(varianceRepository.getVariancesByTypeAndStatus(VarianceType.DEVIATION, WorkflowStatusType.RELEASED));
        typeDto.getDeviationCounts().add(varianceRepository.getVariancesByTypeAndStatus(VarianceType.DEVIATION, WorkflowStatusType.REJECTED));
        typeDto.getDeviationCounts().add(varianceRepository.getOnHoldByVarianceType(VarianceType.DEVIATION));

        typeDto.getWaiverCounts().add(varianceRepository.getNotStartedByVarianceType(VarianceType.WAIVER));
        typeDto.getWaiverCounts().add(varianceRepository.getVariancesByTypeAndStatusAndStarted(VarianceType.WAIVER, WorkflowStatusType.NORMAL, WorkflowStatusType.UNDEFINED));
        typeDto.getWaiverCounts().add(varianceRepository.getVariancesByTypeAndStatus(VarianceType.WAIVER, WorkflowStatusType.RELEASED));
        typeDto.getWaiverCounts().add(varianceRepository.getVariancesByTypeAndStatus(VarianceType.WAIVER, WorkflowStatusType.REJECTED));
        typeDto.getWaiverCounts().add(varianceRepository.getOnHoldByVarianceType(VarianceType.WAIVER));

        return typeDto;
    }

    @Transactional(readOnly = true)
    public ChangeCardCounts getChangeDashboardCardCounts() {
        ChangeCardCounts cardCounts = new ChangeCardCounts();
        cardCounts.setEcrs(ecrRepository.getTotalEcrs());
        cardCounts.setEcos(ecoRepository.getTotalEcos());
        cardCounts.setDcrs(dcrRepository.getTotalDcrs());
        cardCounts.setDcos(dcoRepository.getTotalDcos());
        cardCounts.setMcos(mcoRepository.getTotalMcos());
        cardCounts.setDeviations(varianceRepository.getTotalDeviations(VarianceType.DEVIATION));
        cardCounts.setWaivers(varianceRepository.getTotalWaivers(VarianceType.WAIVER));

        return cardCounts;
    }

    @Transactional(readOnly = true)
    public ChangeTypesDto getChangeTypeCounts() {
        ChangeTypesDto typesDto = new ChangeTypesDto();
        typesDto.getChangeTypes().add(ecrRepository.getTotalEcrs());
        typesDto.getChangeTypes().add(ecoRepository.getTotalEcos());
        typesDto.getChangeTypes().add(dcrRepository.getTotalDcrs());
        typesDto.getChangeTypes().add(dcoRepository.getTotalDcos());
        typesDto.getChangeTypes().add(mcoRepository.getTotalMcos());
        typesDto.getChangeTypes().add(varianceRepository.getTotalDeviations(VarianceType.DEVIATION));
        typesDto.getChangeTypes().add(varianceRepository.getTotalWaivers(VarianceType.WAIVER));
        return typesDto;
    }
}
