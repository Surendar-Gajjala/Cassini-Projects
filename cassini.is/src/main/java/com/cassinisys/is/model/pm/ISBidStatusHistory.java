package com.cassinisys.is.model.pm;
/**
 * Model for ISBidStatusHistory
 */

import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "IS_BIDSTATUSHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PM")
public class ISBidStatusHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "BIDSTATUSHISTORY_ID_GEN",
            sequenceName = "ROW_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "BIDSTATUSHISTORY_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "BID", nullable = false)
    @ApiObjectField(required = true)
    private Integer bid;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE", nullable = false)
    @ApiObjectField(required = true)
    private Date modifiedDate;

    @Column(name = "MODIFIED_BY", nullable = false)
    @ApiObjectField(required = true)
    private Integer modifiedBy;

    @Column(name = "OLD_STATUS")
    @ApiObjectField
    private String oldStatus;

    @Column(name = "NEW_STATUS", nullable = false)
    @ApiObjectField(required = true)
    private String newStatus;

    public ISBidStatusHistory() {
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISBidStatusHistory other = (ISBidStatusHistory) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
