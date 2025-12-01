package com.cassinisys.plm.model.mro;

import com.cassinisys.platform.model.core.Lov;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_WORKORDER_OPERATION")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MROWorkOrderOperation implements Serializable {

    @Id
    @SequenceGenerator(name = "MROWORKORDERCHECKLISTITEM_ID_GEN", sequenceName = "MROWORKORDERCHECKLISTITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MROWORKORDERCHECKLISTITEM_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "WORKORDER")
    private Integer workOrder;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARAM_NAME")
    private String paramName;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.OperationParamValueType")})
    @Column(name = "PARAM_TYPE", nullable = true)
    private OperationParamValueType paramType = OperationParamValueType.NONE;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "LOV", nullable = true)
    private Lov lov;


    @Column(name = "TEXT_VALUE")
    private String textValue;


    @Column(name = "INTEGER_VALUE")
    private Integer integerValue;


    @Column(name = "DECIMAL_VALUE")
    private Double decimalValue;

    @Column(name = "LIST_VALUE")
    private String listValue;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.OperationResult")})
    @Column(name = "RESULT", nullable = true)
    private OperationResult result = OperationResult.NONE;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "BOOLEAN_VALUE")
    private Boolean booleanValue;


}
