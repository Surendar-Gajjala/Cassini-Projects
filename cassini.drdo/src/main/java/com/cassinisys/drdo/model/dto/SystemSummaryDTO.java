package com.cassinisys.drdo.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SystemSummaryDTO implements Serializable {
    private Integer missilesCompleted = 0;
    private Integer missilesInProgress = 0;
    private Integer missilesNotYetStarted = 0;
    private List<MissileStatusDTO> missilesStatuses = new ArrayList<>();

    public Integer getMissilesCompleted() {
        return missilesCompleted;
    }

    public void setMissilesCompleted(Integer missilesCompleted) {
        this.missilesCompleted = missilesCompleted;
    }

    public Integer getMissilesInProgress() {
        return missilesInProgress;
    }

    public void setMissilesInProgress(Integer missilesInProgress) {
        this.missilesInProgress = missilesInProgress;
    }

    public Integer getMissilesNotYetStarted() {
        return missilesNotYetStarted;
    }

    public void setMissilesNotYetStarted(Integer missilesNotYetStarted) {
        this.missilesNotYetStarted = missilesNotYetStarted;
    }

    public List<MissileStatusDTO> getMissilesStatuses() {
        return missilesStatuses;
    }

    public void setMissilesStatuses(List<MissileStatusDTO> missilesStatuses) {
        this.missilesStatuses = missilesStatuses;
    }
}
