package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyam on 17-07-2019.
 */
@Entity
@Table(name = "BDL_RECEIVE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class BDLReceive extends CassiniObject {

    @Column(name = "ISSUE")
    private Integer issue;

    @Column(name = "RECEIVE_SEQUENCE")
    private Integer receiveSequence;

    public BDLReceive() {
        super(DRDOObjectType.BDL_RECEIVE);
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
    }

    public Integer getReceiveSequence() {
        return receiveSequence;
    }

    public void setReceiveSequence(Integer receiveSequence) {
        this.receiveSequence = receiveSequence;
    }
}
