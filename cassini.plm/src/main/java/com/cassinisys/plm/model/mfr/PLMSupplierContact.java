package com.cassinisys.plm.model.mfr;

import com.cassinisys.platform.model.common.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Entity
@Data
@Table(name = "PLM_SUPPLIER_CONTACT")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class PLMSupplierContact implements Serializable {

    @Id
    @SequenceGenerator(name = "SUPPLIERCONTACT_ID_GEN", sequenceName = "SUPPLIERCONTACT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLIERCONTACT_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "SUPPLIER")
    private Integer supplier;

    @Column(name = "CONTACT")
    private Integer contact;

    @Column(name = "ROLE")
    private String role;

    @Transient
    private Person person;
    @Transient
    private PLMSupplier supplierObject;
    @Column(name = "ACTIVE")
    private Boolean active = Boolean.TRUE;

    @Transient
    private Boolean newPerson = Boolean.FALSE;

    @Transient
    private Boolean usedContact = Boolean.FALSE;

}
