package com.cassinisys.test.model;

import com.cassinisys.platform.model.core.DataType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 03-07-2018.
 */
@Entity
@Table(name = "TEST_INPUTPARAM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "TEST")
@Data
public class TestInputParam implements Serializable {
    @Id
    @SequenceGenerator(name = "INPUTPARAM_ID_GEN", sequenceName = "INPUTPARAM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INPUTPARAM_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "TCASE")
    private Integer testCase;
    @ApiObjectField(required = true)
    @Column(name = "DATA_TYPE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.core.DataType")})
    private DataType dataType;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Transient
    private RCInputParamValue rcInputParamValue;

}
