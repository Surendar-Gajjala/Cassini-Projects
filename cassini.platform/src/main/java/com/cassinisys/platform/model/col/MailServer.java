package com.cassinisys.platform.model.col;



import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Rajabrahmachary on 16-11-2018.
 */
@Entity
@Data
@Table(name = "MAILSERVER")
public class MailServer implements Serializable {

    @Id
    @SequenceGenerator(name = "MAILSERVER_ID_GEN",
            sequenceName = "MAILSERVER_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MAILSERVER_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SMTP_SERVER", nullable = false)
    private String smtpServer;


    @Column(name = "SMTP_PORT", nullable = false)
    private Integer smtpPort;

    @Column(name = "IMAP_SERVER", nullable = false)
    private String imapServer;

    @Column(name = "IMAP_PORT", nullable = false)
    private Integer imapPort;



}
