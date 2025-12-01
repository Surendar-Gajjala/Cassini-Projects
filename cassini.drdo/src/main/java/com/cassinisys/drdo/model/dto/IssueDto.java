package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.transactions.IssueHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 18-07-2019.
 */
public class IssueDto {

    List<IssuedItemsDto> issuedItemsDtos = new ArrayList<>();

    List<ReceiveDto> receiveDtoList = new ArrayList<>();

    List<IssueHistory> issueHistories = new ArrayList<>();

    public List<IssuedItemsDto> getIssuedItemsDtos() {
        return issuedItemsDtos;
    }

    public void setIssuedItemsDtos(List<IssuedItemsDto> issuedItemsDtos) {
        this.issuedItemsDtos = issuedItemsDtos;
    }

    public List<ReceiveDto> getReceiveDtoList() {
        return receiveDtoList;
    }

    public void setReceiveDtoList(List<ReceiveDto> receiveDtoList) {
        this.receiveDtoList = receiveDtoList;
    }

    public List<IssueHistory> getIssueHistories() {
        return issueHistories;
    }

    public void setIssueHistories(List<IssueHistory> issueHistories) {
        this.issueHistories = issueHistories;
    }
}
