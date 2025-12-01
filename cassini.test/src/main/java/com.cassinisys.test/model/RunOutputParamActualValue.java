package com.cassinisys.test.model;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Entity
@Table(name = "TEST_RUNOUTPUTPARAMACTUALVALUE")
@Data
public class RunOutputParamActualValue implements Serializable {

    @Id
    @SequenceGenerator(name = "RUNOUTPUTPARAMACTUALVALUE_ID_GEN", sequenceName = "RUNOUTPUTPARAMACTUALVALUE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RUNOUTPUTPARAMACTUALVALUE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer rowId;

    @Column(name = "TCASE")
    private Integer tCase;

    @Column(name = "OUTPUTPARAM")
    private Integer outputparam;

    @Column(name = "STRING_VALUE")
    private String stringValue;

    @Column(name = "INTEGER_VALUE")
    private Integer integerValue;

    @Column(name = "DOUBLE_VALUE")
    private double doubleValue;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_VALUE", nullable = false)
    @ApiObjectField(required = true)
    private Date dateValue;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Column(name = "  TIME_VALUE", nullable = false)
    @ApiObjectField(required = true)
    private Time timeValue;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP_VALUE", nullable = false)
    @ApiObjectField(required = true)
    private Date timeStampValue;

    @Column(name = "BOOLEAN_VALUE")
    private boolean booleanValue;
}
