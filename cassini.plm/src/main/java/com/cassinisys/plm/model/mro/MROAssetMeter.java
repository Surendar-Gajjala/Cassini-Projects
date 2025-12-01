package com.cassinisys.plm.model.mro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Data
@Table(name = "MRO_ASSET_METER")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class MROAssetMeter implements Serializable {

    @Id
    @SequenceGenerator(name = "ASSETMETER_ID_GEN", sequenceName = "ASSETMETER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASSETMETER_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ASSET")
    private Integer asset;

    @Column(name = "METER")
    private Integer meter;

}
