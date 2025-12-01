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
@EqualsAndHashCode(callSuper=false)
@Table(name = "OBJECT_MAILSETTINGS")
@Inheritance(strategy = InheritanceType.JOINED)
public class ObjectMailSettings implements Serializable {

    @Id
    @Column(name = "OBJECTID", nullable = false)
    private Integer objectId;


    @Column(name = "MAILSERVER", nullable = false)
    private Integer mailServer;

    @Column(name = "RECEIVER_USER", nullable = false)
    private String receiverUser;


    @Column(name = "RECEIVER_EMAIL", nullable = false)
    private String receiverEmail;


    @Column(name = "RECEIVER_PASSWORD", nullable = false)
    private String receiverPassword;


    @Column(name = "SENDER_USER", nullable = false)
    private String senderUser;

    @Column(name = "SENDER_EMAIL", nullable = false)
    private String senderEmail;


    @Column(name = "SENDER_PASSWORD", nullable = false)
    private String senderPassword;



}
