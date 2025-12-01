package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomInstanceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 05-02-2019.
 */
public class BomInstanceSectionTarbDto {

    private BomInstanceItem section;

    private BomInstanceItem subsystem;

    private List<BomInstanceItem> sectionParts = new ArrayList<>();

    public BomInstanceItem getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(BomInstanceItem subsystem) {
        this.subsystem = subsystem;
    }

    public BomInstanceItem getSection() {
        return section;
    }

    public void setSection(BomInstanceItem section) {
        this.section = section;
    }

    public List<BomInstanceItem> getSectionParts() {
        return sectionParts;
    }

    public void setSectionParts(List<BomInstanceItem> sectionParts) {
        this.sectionParts = sectionParts;
    }
}
