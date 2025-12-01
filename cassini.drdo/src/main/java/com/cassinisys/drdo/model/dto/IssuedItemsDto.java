package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 19-02-2019.
 */
public class IssuedItemsDto {

    private BomGroup section;

    private List<IssueDetailsDto> issuedItems = new ArrayList<>();

    public BomGroup getSection() {
        return section;
    }

    public void setSection(BomGroup section) {
        this.section = section;
    }

    public List<IssueDetailsDto> getIssuedItems() {
        return issuedItems;
    }

    public void setIssuedItems(List<IssueDetailsDto> issuedItems) {
        this.issuedItems = issuedItems;
    }
}
