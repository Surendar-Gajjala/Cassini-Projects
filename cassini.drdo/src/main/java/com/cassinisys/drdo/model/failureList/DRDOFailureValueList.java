package com.cassinisys.drdo.model.failureList;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Nageshreddy on 02-01-2019.
 */

@Entity
@Table(name = "DRDO_FAILVALUELIST")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "id")
public class DRDOFailureValueList extends CassiniObject {

    @Column(name = "BOMINSTANCEITEM", nullable = false)
    private Integer item;

    @Column(name = "ITEMINSTANCE")
    private Integer instance;

    @Column(name = "LOTINSTANCE")
    private Integer lotInstance;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "SNO")
    private Integer sno;

    @Column(name = "FAILURESTEP")
    private Integer failureStep;

    @Column(name = "CHECKED_BY")
    private Integer checkedBy;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHECKED_DATE")
    private Date checkedDate;

    private DRDOFailureValueList() {
        super(DRDOObjectType.FAILUREVALUELIST);
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getInstance() {
        return instance;
    }

    public void setInstance(Integer instance) {
        this.instance = instance;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getFailureStep() {
        return failureStep;
    }

    public void setFailureStep(Integer failureStep) {
        this.failureStep = failureStep;
    }

    public Date getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
    }

    public Integer getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(Integer checkedBy) {
        this.checkedBy = checkedBy;
    }

    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }

    public Integer getLotInstance() {
        return lotInstance;
    }

    public void setLotInstance(Integer lotInstance) {
        this.lotInstance = lotInstance;
    }
}