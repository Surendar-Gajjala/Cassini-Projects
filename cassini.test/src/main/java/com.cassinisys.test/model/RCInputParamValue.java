package com.cassinisys.test.model;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Created by GSR on 01-08-2018.
 */
@Entity
@Table(name = "TEST_RCINPUTPARAMVALUE")
@Data
public class RCInputParamValue implements Serializable {
    @Id
    @SequenceGenerator(name = "RCINPUTPARAMVALUE_ID_GEN", sequenceName = "RCINPUTPARAMVALUE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RCINPUTPARAMVALUE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;
    @ApiObjectField(required = true)
    @Column(name = "CONFIG")
    private Integer config;
    @ApiObjectField(required = true)
    @Column(name = "TCASE")
    private Integer testCase;
    @ApiObjectField(required = true)
    @Column(name = "INPUTPARAM")
    private Integer inputParam;
    @Column(name = "STRING_VALUE")
    private String stringValue;
    @Column(name = "INTEGER_VALUE")
    private Integer integerValue;
    @Column(name = "DOUBLE_VALUE")
    private double doubleValue;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
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
