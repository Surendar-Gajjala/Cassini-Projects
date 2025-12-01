package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomInstanceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 16-09-2019.
 */
public class SubsystemIssueReport {

    BomInstanceItem subsystem;

    List<BomInstanceItemDto> issuedParts = new ArrayList<>();

    public BomInstanceItem getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(BomInstanceItem subsystem) {
        this.subsystem = subsystem;
    }

    public List<BomInstanceItemDto> getIssuedParts() {
        return issuedParts;
    }

    public void setIssuedParts(List<BomInstanceItemDto> issuedParts) {
        this.issuedParts = issuedParts;
    }
}
