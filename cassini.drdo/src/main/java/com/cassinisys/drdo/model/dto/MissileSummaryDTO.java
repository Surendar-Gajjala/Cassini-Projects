package com.cassinisys.drdo.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MissileSummaryDTO implements Serializable {
    private Integer sectionsCompleted = 0;
    private Integer sectionsInProgress = 0;
    private Integer sectionsNotYetStarted = 0;
    private List<SectionStatusDTO> sectionsStatuses = new ArrayList<>();

    public Integer getSectionsCompleted() {
        return sectionsCompleted;
    }

    public void setSectionsCompleted(Integer sectionsCompleted) {
        this.sectionsCompleted = sectionsCompleted;
    }

    public Integer getSectionsInProgress() {
        return sectionsInProgress;
    }

    public void setSectionsInProgress(Integer sectionsInProgress) {
        this.sectionsInProgress = sectionsInProgress;
    }

    public Integer getSectionsNotYetStarted() {
        return sectionsNotYetStarted;
    }

    public void setSectionsNotYetStarted(Integer sectionsNotYetStarted) {
        this.sectionsNotYetStarted = sectionsNotYetStarted;
    }

    public List<SectionStatusDTO> getSectionsStatuses() {
        return sectionsStatuses;
    }

    public void setSectionsStatuses(List<SectionStatusDTO> sectionsStatuses) {
        this.sectionsStatuses = sectionsStatuses;
    }
}
