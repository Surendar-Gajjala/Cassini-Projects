package com.cassinisys.plm.model.mro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Entity
@Table(name = "MRO_ASSET_SPAREPART")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class MROAssetSparePart implements Serializable {

    @Id
    @SequenceGenerator(name = "ASSETSPAREPART_ID_GEN", sequenceName = "ASSETSPAREPART_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASSETSPAREPART_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ASSET")
    private Integer asset;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PART")
    private MROSparePart sparePart;

}
