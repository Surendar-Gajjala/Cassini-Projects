package com.cassinisys.platform.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "PORTAL_ACCOUNT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalAccount implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id = 1;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "AUTH_KEY")
    private String authKey;
}
