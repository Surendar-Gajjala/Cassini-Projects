package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.Bom;
import com.cassinisys.drdo.model.bom.BomInstance;
import com.cassinisys.drdo.model.bom.BomInstanceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 05-02-2019.
 */
public class BomInstanceTarbDto {

    private Bom system;

    private BomInstance bomInstance;

    private BomInstanceItem section;

    private List<BomInstanceSectionTarbDto> sectionTarbDtoList = new ArrayList<>();

    public Bom getSystem() {
        return system;
    }

    public void setSystem(Bom system) {
        this.system = system;
    }

    public BomInstance getBomInstance() {
        return bomInstance;
    }

    public void setBomInstance(BomInstance bomInstance) {
        this.bomInstance = bomInstance;
    }

    public List<BomInstanceSectionTarbDto> getSectionTarbDtoList() {
        return sectionTarbDtoList;
    }

    public void setSectionTarbDtoList(List<BomInstanceSectionTarbDto> sectionTarbDtoList) {
        this.sectionTarbDtoList = sectionTarbDtoList;
    }

    public BomInstanceItem getSection() {
        return section;
    }

    public void setSection(BomInstanceItem section) {
        this.section = section;
    }
}
