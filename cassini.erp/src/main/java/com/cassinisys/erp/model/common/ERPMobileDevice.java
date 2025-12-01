package com.cassinisys.erp.model.common;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by reddy on 10/24/15.
 */
@Entity
@Table (name = "ERP_MOBILEDEVICE")
@ApiObject (group = "CORE")
public class ERPMobileDevice implements Serializable {
    @ApiObjectField (required = true)
    private String deviceId;
    @ApiObjectField (required = true)
    private String senderId;
    @ApiObjectField
    private String os;
    @ApiObjectField
    private String osVersion;
    @ApiObjectField
    private String deviceInfo;
    @ApiObjectField
    private Boolean disablePushNotification = Boolean.FALSE;


    @Id
    @Column(name = "DEVICE_ID")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column (name = "SENDER_ID")
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @Column (name = "OS")
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Column (name = "OS_VERSION")
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Column (name = "DEVICE_INFO")
    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    @Column (name = "DISABLE_PUSHNOTIFICATION")
    public Boolean getDisablePushNotification() {
        return disablePushNotification;
    }

    public void setDisablePushNotification(Boolean disablePushNotification) {
        this.disablePushNotification = disablePushNotification;
    }
}
