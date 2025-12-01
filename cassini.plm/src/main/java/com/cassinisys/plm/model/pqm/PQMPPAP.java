package com.cassinisys.plm.model.pqm;

import javax.persistence.*;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_PPAP")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMPPAP extends CassiniObject {

    @Column(name = "NUMBER",nullable = false)
    private String number;

    @Column(name = "NAME",nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PQMPPAPType type;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SUPPLIER",nullable = false)
    private Integer supplier;

    @Column(name = "SUPPLIER_PART",nullable = false)
    private Integer supplierPart;

    @OneToOne
    @JoinColumn(name = "STATUS")
    private PLMLifeCyclePhase status;


    @Transient
    private String supplierName;

    @Transient
    private PLMManufacturerPart mfrPart;

    @Transient
    private String createdPerson;
    @Transient
    private String modifiedPerson;

    public PQMPPAP() {
        super.setObjectType(PLMObjectType.PPAP);
    }


} 