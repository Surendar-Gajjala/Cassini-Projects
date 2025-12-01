package com.cassinisys.plm.model.mro;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_METER_READING")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class MROMeterReading implements Serializable {

    @Id
    @SequenceGenerator(name = "METERREADING_ID_GEN", sequenceName = "METERREADING_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "METERREADING_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date currentDate = new Date();
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ASSET_METER")
    private MROAssetMeter assetMeter;

    @Column(name = "VALUE")
    private Double value;


}
