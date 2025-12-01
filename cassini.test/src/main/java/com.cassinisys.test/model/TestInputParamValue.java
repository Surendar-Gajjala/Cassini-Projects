package com.cassinisys.test.model;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "TEST_INPUTPARAMVALUE")
@ApiObject(group = "TEST")
@Data
public class TestInputParamValue implements Serializable {

    @Id
    @SequenceGenerator(name = "INPUTPARAMVALUE_ID_GEN", sequenceName = "INPUTPARAMVALUE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INPUTPARAMVALUE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer rowId;

    @Column(name = "RUN_CONFIG")
    private Integer runConfig;

    @Column(name = "TCASE")
    private Integer tCase;

    @Column(name = "INPUTPARAM")
    private Integer inputParam;

    @Column(name = "STRING_VALUE")
    private String stringValue;

    @Column(name = "INTEGER_VALUE")
    private Integer integerValue;

    @Column(name = "DOUBLE_VALUE")
    private double doubleValue;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_VALUE")
    private Date dateValue;

    @Column(name = "TIME_VALUE", nullable = false)
    @ApiObjectField(required = true)
    private Time timeValue;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP_VALUE")
    private Date timestampValue;

    @Column(name = "BOOLEAN_VALUE")
    private boolean booleanValue;
}
