package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_CUSTOMER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMCustomer extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CONTACT_PERSON")
    private Integer contactPerson;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private Person person;

    @Transient
    private Boolean used = Boolean.FALSE;

    public PQMCustomer() {
        super.setObjectType(PLMObjectType.CUSTOMER);
    }


}
