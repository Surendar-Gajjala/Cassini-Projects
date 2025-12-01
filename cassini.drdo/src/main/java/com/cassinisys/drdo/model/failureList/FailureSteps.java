package com.cassinisys.drdo.model.failureList;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 02-01-2019.
 */
@Entity
@Table(name = "FAILURESTEPS")
public class FailureSteps implements Serializable {

    @Id
    @SequenceGenerator(name = "LISTSTEP_ID_GEN", sequenceName = "LISTSTEP_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LISTSTEP_ID_GEN")
    private Integer id;

    @Column(name = "SERIAL_NO")
    private Integer serialNo;

    @Column(name = "FAILURELIST")
    private Integer failureList;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DATATYPE")
    private String dataType;

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

    public Integer getFailureList() {
        return failureList;
    }

    public void setFailureList(Integer failureList) {
        this.failureList = failureList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
