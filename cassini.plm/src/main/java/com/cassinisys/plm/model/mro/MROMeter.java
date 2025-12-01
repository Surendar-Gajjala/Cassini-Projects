package com.cassinisys.plm.model.mro;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_METER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MROMeter extends MROObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MROMeterType type;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.MeterType")})
    @Column(name = "METER_TYPE", nullable = true)
    private MeterType meterType = MeterType.CONTINUOUS;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.mro.MeterReadingType")})
    @Column(name = "READING_TYPE", nullable = true)
    private MeterReadingType meterReadingType = MeterReadingType.ABSOLUTE;

    @Column(name = "QOM")
    private Integer qom;

    @Column(name = "UOM")
    private Integer uom;

    @Transient
    private String measurementName;
    @Transient
    private String unitName;

    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String imageValue;
    @Transient
    private String meterTypeName;
    @Transient
    private String meterReadingTypeName;

    @Transient
    private String modifiedPerson;

    @Transient
    private Double lastReadingValue;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Transient
    private Date lastReadingDate;

    public MROMeter() {
        super.setObjectType(MROEnumObject.MROMETER);
    }

    @Transient
    private String assignedAsset;
}
