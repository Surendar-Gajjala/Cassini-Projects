package com.cassinisys.plm.model.mfr;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_SUPPLIER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMSupplier extends CassiniObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PLMSupplierType supplierType;
    @Column(name = "NAME")
    private String name;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "CITY")
    private String city;
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @Column(name = "FAX_NUMBER")
    private String faxNumber;

    @Column(name = "EMAIL")
    private String email;
    @Column(name = "WEBSITE")
    private String webSite;
    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;
    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String modifiedPerson;
    @Transient
    private Boolean usedSupplier = Boolean.FALSE;
    @Transient
    private List<PLMSupplierAttribute> supplierAttributes = new ArrayList<>();
    @Transient
    private List<ObjectAttribute> objectAttributes = new ArrayList<>();

    public PLMSupplier() {
        super(PLMObjectType.MFRSUPPLIER);
    }

}

