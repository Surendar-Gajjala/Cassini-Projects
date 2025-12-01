package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.transactions.BDLReceive;
import com.cassinisys.platform.model.common.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 18-07-2019.
 */
public class ReceiveDto {

    private BDLReceive receive;

    private List<IssueDetailsDto> issueItems = new ArrayList<>();

    private List<ReceiveItemDto> receiveItems = new ArrayList<>();

    private Person receivedBy;

    public BDLReceive getReceive() {
        return receive;
    }

    public void setReceive(BDLReceive receive) {
        this.receive = receive;
    }

    public List<IssueDetailsDto> getIssueItems() {
        return issueItems;
    }

    public void setIssueItems(List<IssueDetailsDto> issueItems) {
        this.issueItems = issueItems;
    }

    public Person getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(Person receivedBy) {
        this.receivedBy = receivedBy;
    }

    public List<ReceiveItemDto> getReceiveItems() {
        return receiveItems;
    }

    public void setReceiveItems(List<ReceiveItemDto> receiveItems) {
        this.receiveItems = receiveItems;
    }
}
