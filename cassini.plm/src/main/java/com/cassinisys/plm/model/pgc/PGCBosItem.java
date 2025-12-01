package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 25-11-2020.
 */
@Entity
@Table(name = "PGC_BOS_ITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class PGCBosItem implements Serializable {

    @Id
    @SequenceGenerator(name = "BOSITEM_ID_GEN", sequenceName = "BOSITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOSITEM_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "BOS")
    private Integer bos;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pgc.BosItemType")})
    @Column(name = "TYPE", nullable = true)
    private BosItemType bosItemType = BosItemType.SUBSTANCE;

    @Column(name = "MATERIAL")
    private Integer material;

    @Column(name = "SUBSTANCE")
    private Integer substance;

    @Column(name = "SUBSTANCE_GROUP")
    private Integer substanceGroup;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "MASS")
    private Double mass;

    @Column(name = "UOM")
    private Integer uom;
}

