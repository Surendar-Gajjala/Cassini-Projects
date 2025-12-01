package com.cassinisys.plm.model.mes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by smukka on 28-07-2022.
 */
@Entity
@Data
@Table(name = "MES_BOPINSTANCE_OPERATION_PART")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MESBOPInstanceOperationPart implements Serializable {

    @Id
    @SequenceGenerator(name = "BOPPLAN_ID_GEN", sequenceName = "BOPPLAN_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOPPLAN_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "BOP_OPERATION")
    private Integer bopOperation;

    @Column(name = "MBOMINSTANCE_ITEM")
    private Integer mbomInstanceItem;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mes.OperationPartType")})
    @Column(name = "TYPE")
    private OperationPartType type = OperationPartType.CONSUMED;

    @Column(name = "NOTES")
    private String notes;
}
