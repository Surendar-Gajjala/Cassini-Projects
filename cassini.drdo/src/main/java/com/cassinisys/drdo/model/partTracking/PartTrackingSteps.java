package com.cassinisys.drdo.model.partTracking;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Entity
@Table(name = "PARTTRACKINGSTEPS")
public class PartTrackingSteps implements Serializable {

    @Id
    @SequenceGenerator(name = "LISTSTEP_ID_GEN", sequenceName = "LISTSTEP_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LISTSTEP_ID_GEN")
    private Integer id;

    @Column(name = "SERIAL_NO")
    private Integer serialNo;

    @Column(name = "PARTTRACKING")
    private Integer partTracking;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SCAN")
    private Boolean scan;

    @Column(name = "BDL")
    private Boolean bdl;

    @Column(name = "SSQAG")
    private Boolean ssqag;

    @Column(name = "CAS")
    private Boolean cas;

    @Column(name = "ATTACHMENT")
    private Boolean attachment;

    @Column(name = "PERCENTAGE")
    private Double percentage;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getPartTracking() {
        return partTracking;
    }

    public void setPartTracking(Integer partTracking) {
        this.partTracking = partTracking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getScan() {
        return scan;
    }

    public void setScan(Boolean scan) {
        this.scan = scan;
    }

    public Boolean getBdl() {
        return bdl;
    }

    public void setBdl(Boolean bdl) {
        this.bdl = bdl;
    }

    public Boolean getSsqag() {
        return ssqag;
    }

    public void setSsqag(Boolean ssqag) {
        this.ssqag = ssqag;
    }

    public Boolean getCas() {
        return cas;
    }

    public void setCas(Boolean cas) {
        this.cas = cas;
    }

    public Boolean getAttachment() {
        return attachment;
    }

    public void setAttachment(Boolean attachment) {
        this.attachment = attachment;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
