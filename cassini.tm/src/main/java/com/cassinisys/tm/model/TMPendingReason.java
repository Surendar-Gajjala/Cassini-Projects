package com.cassinisys.tm.model;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 30-08-2016.
 */

@Entity
@Table(name = "PENDING_REASON")
@ApiObject(name = "TM")
public class TMPendingReason implements Serializable {

    @Id
    @SequenceGenerator(name = "PENDINGREASON_ID_GEN", sequenceName = "PENDINGREASON_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PENDINGREASON_ID_GEN")
    @Column(name = "REASON_ID", nullable = false)
    private Integer reasonId;

    @Column(name = "REASON", nullable = false)
    @ApiObjectField(required = true)
    private String reason;


    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
