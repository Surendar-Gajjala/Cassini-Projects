package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomInstanceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 29-01-2019.
 */
public class RequestSectionDto {

    private BomInstanceItem section;

    private List<BomInstanceItem> bomInstanceItems = new ArrayList<>();

    private List<BomInstanceItem> children = new ArrayList<>();

    private List<BomInstanceItem> notYetRequestedItems = new ArrayList<>();

    private List<ItemDetailsReportDto> itemDetailsReportDtos = new ArrayList<>();

    public BomInstanceItem getSection() {
        return section;
    }

    public void setSection(BomInstanceItem section) {
        this.section = section;
    }

    public List<BomInstanceItem> getBomInstanceItems() {
        return bomInstanceItems;
    }

    public void setBomInstanceItems(List<BomInstanceItem> bomInstanceItems) {
        this.bomInstanceItems = bomInstanceItems;
    }

    public List<BomInstanceItem> getNotYetRequestedItems() {
        return notYetRequestedItems;
    }

    public void setNotYetRequestedItems(List<BomInstanceItem> notYetRequestedItems) {
        this.notYetRequestedItems = notYetRequestedItems;
    }

    public List<BomInstanceItem> getChildren() {
        return children;
    }

    public void setChildren(List<BomInstanceItem> children) {
        this.children = children;
    }

    public List<ItemDetailsReportDto> getItemDetailsReportDtos() {
        return itemDetailsReportDtos;
    }

    public void setItemDetailsReportDtos(List<ItemDetailsReportDto> itemDetailsReportDtos) {
        this.itemDetailsReportDtos = itemDetailsReportDtos;
    }
}
