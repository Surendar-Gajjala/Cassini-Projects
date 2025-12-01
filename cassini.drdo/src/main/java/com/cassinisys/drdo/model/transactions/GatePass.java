package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.File;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Nageshreddy on 27-10-2018.
 */
@Entity
@Table(name = "GATEPASS")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatePass extends CassiniObject {

    @ApiObjectField
    @Column(name = "GATEPASS_NUMBER")
    private String gatePassNumber;

    @ApiObjectField
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GATEPASS")
    private File gatePass;

    @ApiObjectField
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GATEPASS_DATE")
    private Date gatePassDate;

    @Column(name = "FINISH")
    private Boolean finish = Boolean.FALSE;

    @Transient
    private Integer inwards = 0;

    @Transient
    private Double quantity = 0.0;

    public GatePass() {
        super(DRDOObjectType.INWARDGATEPASS);
    }

    public String getGatePassNumber() {
        return gatePassNumber;
    }

    public void setGatePassNumber(String gatePassNumber) {
        this.gatePassNumber = gatePassNumber;
    }

    public File getGatePass() {
        return gatePass;
    }

    public void setGatePass(File gatePass) {
        this.gatePass = gatePass;
    }

    public Date getGatePassDate() {
        return gatePassDate;
    }

    public void setGatePassDate(Date gatePassDate) {
        this.gatePassDate = gatePassDate;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public Integer getInwards() {
        return inwards;
    }

    public void setInwards(Integer inwards) {
        this.inwards = inwards;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
