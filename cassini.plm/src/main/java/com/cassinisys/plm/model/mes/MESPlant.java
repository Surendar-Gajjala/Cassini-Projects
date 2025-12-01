package com.cassinisys.plm.model.mes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.cassinisys.plm.model.plm.dto.FileDto;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Entity
@Table(name = "MES_PLANT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESPlant extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESPlantType type;

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
    private String faxAddress;

    @Column(name = "EMAIL")
    private String email;


    @Column(name = "PLANT_MANAGER")
    private Integer plantPerson;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "REQUIRES_MAINTENANCE", nullable = false)
    private Boolean requiresMaintenance = Boolean.TRUE;

    @Transient
    private String person;

    @Transient
    private String createPerson;
    @Transient
    private String typeName;

    @Transient
    private Boolean workCenterObject = Boolean.FALSE;

    @Transient
    private String modifiedPerson;

    public MESPlant() {
        super.setObjectType(MESEnumObject.PLANT);
    }

    @Transient
    private List<FileDto> plantFiles = new LinkedList<>();


}
