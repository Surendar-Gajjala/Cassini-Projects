package com.cassinisys.plm.model.pm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "PROJECT_EMAILSETTINGS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMProjectEmailSetting implements Serializable {

    @Id
    @Column(name = "PROJECT")
    private Integer project;

    @Column(name = "USERNAME")
    private String user;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "IMAP_SERVER")
    private String imapServer;

    @Column(name = "IMAP_PORT")
    private Integer imapPort;

    @Column(name = "SMTP_SERVER")
    private String smtpServer;

    @Column(name = "SMTP_PORT")
    private Integer smtpPort;

}
