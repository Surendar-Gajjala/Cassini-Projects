package com.cassinisys.plm.model.mes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by smukka on 28-07-2022.
 */
@Entity
@Data
@Table(name = "MES_BOP_OPERATION_INSTRUCTIONS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MESBOPOperationInstructions implements Serializable {
    @Id
    @SequenceGenerator(name = "BOPPLAN_ID_GEN", sequenceName = "BOPPLAN_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOPPLAN_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "BOP_OPERATION")
    private Integer bopOperation;

    @Column(name = "INSTRUCTIONS")
    private String instructions;
}
