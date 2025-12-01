package com.cassinisys.drdo.model.partTracking;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Entity
@Table(name = "PARTTRACKING_SCANNED_UPN")
public class PartTrackingScannedUPN implements Serializable {

    @Id
    @SequenceGenerator(name = "PARTTRACKINGSCANUPN_ID_GEN", sequenceName = "PARTTRACKINGSCANUPN_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTTRACKINGSCANUPN_ID_GEN")
    private Integer id;

    @Column(name = "TRACK_ID")
    private Integer partTrackingId;

    @Column(name = "UPN")
    private String upn;

    @Column(name = "FAIL")
    private Boolean fail = Boolean.FALSE;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPartTrackingId() {
        return partTrackingId;
    }

    public void setPartTrackingId(Integer partTrackingId) {
        this.partTrackingId = partTrackingId;
    }

    public String getUpn() {
        return upn;
    }

    public void setUpn(String upn) {
        this.upn = upn;
    }

    public Boolean getFail() {
        return fail;
    }

    public void setFail(Boolean fail) {
        this.fail = fail;
    }
}
