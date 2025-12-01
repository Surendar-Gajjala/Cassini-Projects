package com.cassinisys.drdo.service;

import com.cassinisys.drdo.model.bom.BomInstance;
import com.cassinisys.drdo.model.bom.BomInstanceItem;
import com.cassinisys.drdo.model.dto.*;
import com.cassinisys.drdo.model.partTracking.PartTrackingItems;
import com.cassinisys.drdo.model.partTracking.PartTrackingSteps;
import com.cassinisys.drdo.model.partTracking.TrackValue;
import com.cassinisys.drdo.repo.bom.BomInstanceItemRepository;
import com.cassinisys.drdo.repo.partTracking.PartTrackingItemsRepository;
import com.cassinisys.drdo.repo.partTracking.PartTrackingStepsRepository;
import com.cassinisys.drdo.repo.partTracking.TrackValueRepository;
import com.cassinisys.drdo.service.bom.BomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingService {

    @Autowired
    private BomService bomService;
    @Autowired
    private BomInstanceItemRepository bomInstanceItemRepository;
    @Autowired
    private PartTrackingItemsRepository partTrackingItemsRepository;
    @Autowired
    private TrackValueRepository trackValueRepository;
    @Autowired
    private PartTrackingStepsRepository partTrackingStepsRepository;

    public SystemSummaryDTO getSystemSummary(Integer id) {
        SystemSummaryDTO summaryDTO = new SystemSummaryDTO();

        List<BomInstance> bomInstances = bomService.getBomInstances(id);
        for (BomInstance bomInstance : bomInstances) {
            if (bomInstance.getPercentage() == null || bomInstance.getPercentage() == 0) {
                summaryDTO.setMissilesNotYetStarted(summaryDTO.getMissilesNotYetStarted() + 1);
            } else if (bomInstance.getPercentage() >= 100) {
                summaryDTO.setMissilesCompleted(summaryDTO.getMissilesCompleted() + 1);
            } else {
                summaryDTO.setMissilesInProgress(summaryDTO.getMissilesInProgress() + 1);
            }

            MissileStatusDTO dto = new MissileStatusDTO();
            dto.setId(bomInstance.getId());
            dto.setName(bomInstance.getItem().getInstanceName());
            if (bomInstance.getPercentage() != null && bomInstance.getPercentage() > 100)
                bomInstance.setPercentage(100D);
            dto.setPercentComplete(bomInstance.getPercentage());
            dto.setOnHold(checkOnHoldOrNot(bomInstance.getId(), dto));

            summaryDTO.getMissilesStatuses().add(dto);
        }

        return summaryDTO;
    }

    private Boolean checkOnHoldOrNot(Integer id, SummaryStatusDTO dto) {
        List<Integer> integerList = partTrackingItemsRepository.getIdsByItem(id);
        List<TrackValue> trackValues1 = trackValueRepository.findByPartTrackingInOrderByPartTrackingAsc(integerList);
        if (trackValues1.size() > 0) {
            TrackValue startValue = trackValues1.get(0);
            dto.setInitialiseDate(startValue.getCreatedDate());
            TrackValue trackValue = trackValues1.get(trackValues1.size() - 1);
            if (dto.getPercentComplete() == 100)
                dto.setCompletedDate(trackValue.getCreatedDate());

            PartTrackingItems partTrackingItems = partTrackingItemsRepository.findOne(trackValue.getPartTracking());
            PartTrackingSteps partTrackingSteps = partTrackingStepsRepository.findOne(partTrackingItems.getPartTrackingStep());
            dto.setPartTrackingStep(partTrackingSteps.getDescription());
        }
        List<TrackValue> trackValues = trackValueRepository.findByPartTrackingInAndCheckedFalse(integerList);
        if (trackValues.size() > 0) {
            dto.setToolTip(trackValues.get(0).getComment());
            return true;
        }
        return false;
    }

    public MissileSummaryDTO getMissileSummary(Integer id) {
        MissileSummaryDTO summaryDTO = new MissileSummaryDTO();
        List<BomInstanceItem> sections = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(id);
        for (BomInstanceItem section : sections) {
            if (section.getPercentage() == null || section.getPercentage() == 0) {
                summaryDTO.setSectionsNotYetStarted(summaryDTO.getSectionsNotYetStarted() + 1);
            } else if (section.getPercentage() >= 100) {
                summaryDTO.setSectionsCompleted(summaryDTO.getSectionsCompleted() + 1);
            } else {
                summaryDTO.setSectionsInProgress(summaryDTO.getSectionsInProgress() + 1);
            }

            SectionStatusDTO dto = new SectionStatusDTO();
            dto.setId(section.getId());
            dto.setName(section.getTypeRef().getName());
            if (section.getPercentage() != null && section.getPercentage() > 100)
                section.setPercentage(100D);
            dto.setPercentComplete(section.getPercentage());
            dto.setOnHold(checkOnHoldOrNot(section.getId(), dto));
            summaryDTO.getSectionsStatuses().add(dto);
        }

        return summaryDTO;
    }
}
