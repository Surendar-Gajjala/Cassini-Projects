package com.cassinisys.drdo.model.transactions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 17-07-2019.
 */
@Entity
@Table(name = "BDL_RECEIVEITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class BDLReceiveItem implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "DISPATCH_ITEM_ID_GEN", sequenceName = "DISPATCH_ITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DISPATCH_ITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "RECEIVE")
    private Integer receive;

    @Column(name = "ISSUE_ITEM")
    private Integer issueItem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReceive() {
        return receive;
    }

    public void setReceive(Integer receive) {
        this.receive = receive;
    }

    public Integer getIssueItem() {
        return issueItem;
    }

    public void setIssueItem(Integer issueItem) {
        this.issueItem = issueItem;
    }
}
