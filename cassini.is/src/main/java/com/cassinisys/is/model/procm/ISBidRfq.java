package com.cassinisys.is.model.procm;
/**
 * Model for ISBidRfq
 */

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_BIDRFQ")
@PrimaryKeyJoinColumn(name = "RFQ_ID")
@ApiObject(name = "PROCM")
public class ISBidRfq extends ISRfq {

    private static final long serialVersionUID = 1L;

    @Column(name = "BID", nullable = false)
    @ApiObjectField(required = true)
    private Integer bid;

    public ISBidRfq() {
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }
}
