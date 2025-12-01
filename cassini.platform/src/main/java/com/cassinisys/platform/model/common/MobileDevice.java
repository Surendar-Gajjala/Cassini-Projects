package com.cassinisys.platform.model.common;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "MOBILEDEVICE")
@Data
public class MobileDevice implements Serializable {

    @Id
    @Column(name = "DEVICE_ID")
    private String deviceId;

    @Column(name = "SENDER_ID")
    private String senderId;

    @Column(name = "OS")
    private String os;

    @Column(name = "OS_VERSION")
    private String osVersion;

    @Column(name = "MANUFACTURER")
    private String manufacturer;

    @Column(name = "DEVICE_INFO")
    private String deviceInfo;

    @Column(name = "DISABLE_PUSHNOTIFICATION")
    private Boolean disablePushNotification = Boolean.FALSE;

    @Transient
    private Integer personId;

    @Transient
    private Integer sessionId;

}
