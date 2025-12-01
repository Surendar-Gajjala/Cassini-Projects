package com.cassinisys.plm.model.mfr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Suresh Cassini on 2-11-2020.
 */
@Entity
@Data
@Table(name = "PLM_SUPPLIER_PART")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class PLMSupplierPart implements Serializable {

    @Id
    @SequenceGenerator(name = "SUPPLIERPART_ID_GEN", sequenceName = "SUPPLIERPART_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLIERPART_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "SUPPLIER")
    private Integer supplier;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PART", nullable = false)
    private PLMManufacturerPart manufacturerPart;
    @Column(name = "PART_NUMBER")
    private String partNumber;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "MODIFIED_DATE",
            nullable = false
    )
    private Date modifiedDate = new Date();

    @Transient
    private PLMSupplier supplierObject;

    @Transient
    private Boolean assignedDeclarationPart;
}
