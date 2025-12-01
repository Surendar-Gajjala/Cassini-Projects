package com.cassinisys.plm.model.mes;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MES_WORKCENTER_OPERATION")
@Data
public class MESWorkCenterOperation implements Serializable {

    @Id
    @SequenceGenerator(name = "WORKCENTEROPERATION_ID_GEN", sequenceName = "WORKCENTEROPERATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKCENTEROPERATION_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "WORK_CENTER")
    private Integer workCenter;

    @Column(name = "OPERATION")
    private Integer operation;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private MESOperation operationObject;

    @Transient
    private String modifiedName;

}
