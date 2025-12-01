package com.cassinisys.test.model;

import com.cassinisys.platform.model.core.DataType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Entity
@Table(name = "TEST_RUNOUTPUTPARAM")
@Data
public class RunOutPutParam implements Serializable {

    @Id
    @SequenceGenerator(name = "RUNOUTPUTPARAM_ID_GEN", sequenceName = "RUNOUTPUTPARAM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RUNOUTPUTPARAM_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TCASE")
    private RunCase tCase;

    @ApiObjectField(required = true)
    @Column(name = "DATA_TYPE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.core.DataType")})
    private DataType dataType;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Transient
    private RunOutPutParamExpectedValue runOutPutParamExpectedValue;
    @Transient
    private RunOutputParamActualValue runOutputParamActualValue;

}
