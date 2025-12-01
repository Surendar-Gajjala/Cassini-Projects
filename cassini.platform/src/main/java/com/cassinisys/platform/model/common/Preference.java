package com.cassinisys.platform.model.common;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Created by raghav on 18-11-2019.
 */
@Entity
@Table(name = "PREFERENCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Preference implements Serializable {

    @Id
    @SequenceGenerator(name = "PREFERENCE_ID_GEN", sequenceName = "PREFERENCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PREFERENCE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CONTEXT")
    private String context;

    @Column(name = "PREFERENCE_KEY")
    private String preferenceKey;

    @Column(name = "STRING_VALUE")
    private String stringValue;

    @Column(name = "INTEGER_VALUE")
    private Integer integerValue;

    @Column(name = "DECIMAL_VALUE")
    private Double decimalValue;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_VALUE")
    private Date dateValue;

    @Column(name = "TIME_VALUE")
    private Time timeValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATETIME_VALUE")
    private Date datetimeValue;

    @Column(name = "BOOLEAN_VALUE")
    private Boolean booleanValue;

    @Column(name = "CUSTOM_LOGO")
    private byte[] customLogo;

    @Type(
            type = "com.cassinisys.platform.util.converter.StringArrayType"
    )
    @Column(name = "STRING_ARRAY_VALUE")
    private String[] stringArrayValue;

    @Type(
            type = "com.cassinisys.platform.util.converter.IntArrayUserType"
    )
    @Column(name = "INTEGER_ARRAY_VALUE")
    private Integer[] integerArrayValue;

    @Column(name = "JSON_VALUE")
    private String jsonValue;

    @Transient
    private DefaultValueDto defaultValue;
    @Transient
    private String defaultValueName;
}
