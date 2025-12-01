package com.cassinisys.platform.model.common;

import com.cassinisys.platform.config.PersonEntityListener;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by reddy on 7/18/15.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PERSON")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "PERSON_ID")
@EntityListeners(PersonEntityListener.class)
public class Person extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "PERSON_TYPE", nullable = false)
    private Integer personType;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "PHONE_OFFICE")
    private String phoneOffice;

    @Column(name = "PHONE_MOBILE")
    private String phoneMobile;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "EMAIL_VERIFIED")
    private Boolean emailVerified = Boolean.FALSE;

    @JsonIgnore
    @Column(name = "IMAGE")
    private byte[] image;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "MOBILE_DEVICE", insertable = true,
            updatable = true, nullable = true, unique = true)
    private MobileDevice mobileDevice;

    @Column(name = "DEFAULT_GROUP")
    private Integer defaultGroup;

    @Transient
    private PersonType type;

    @Transient
    private String login;

    @Transient
    private boolean external = false;

    @Transient
    private boolean isActive = false;

    @Transient
    private boolean hasImage = false;
    @Transient
    private Boolean sendPasscode = Boolean.FALSE;


    public Person() {
        super(ObjectType.PERSON);
    }

    public String getFullName() {
        return firstName + " " + (lastName != null && !lastName.trim().isEmpty() ? lastName : "");
    }

}
