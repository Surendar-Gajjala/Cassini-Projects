package com.cassinisys.is.model.tm;
/* Model for  ISBidTask */

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_BIDTASK")
@PrimaryKeyJoinColumn(name = "TASK_ID")
@ApiObject(name = "TM")
public class ISBidTask extends ISTask {

    private static final long serialVersionUID = 1L;

    @Column(name = "BID", nullable = false)
    @ApiObjectField(required = true)
    private Integer bid;

    public ISBidTask() {
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

}
