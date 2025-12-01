package com.cassinisys.is.model.col;
/* Model For ISBid Meting */

import com.cassinisys.platform.model.col.Meeting;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_BIDMEETING")
@PrimaryKeyJoinColumn(name = "MEETING_ID")
@ApiObject(name = "COL")
public class ISBidMeeting extends Meeting {

    private static final long serialVersionUID = 1L;

    @ApiObjectField(required = true)
    @Column(name = "BID", nullable = false)
    private Integer bid;

    public ISBidMeeting() {
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

}
