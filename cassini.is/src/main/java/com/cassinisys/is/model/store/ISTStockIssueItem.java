package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.*;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Entity
@Table(name = "IS_STOCKISSUEITEM")
@PrimaryKeyJoinColumn(name = "ROWID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class ISTStockIssueItem extends ISTopStockMovement {

    @Column
    private Integer issue;

    @Transient
    private Integer boqReference;

    public ISTStockIssueItem() {
        super(ISObjectType.ISSUEITEM);
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
    }

    public Integer getBoqReference() {
        return boqReference;
    }

    public void setBoqReference(Integer boqReference) {
        this.boqReference = boqReference;
    }
}
