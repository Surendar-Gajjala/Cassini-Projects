package com.cassinisys.platform.service.notification.sms;

/**
 * Created by reddy on 9/7/15.
 */
public class PasswordResetSms extends SMSObject {

    public PasswordResetSms() {
        super.setTemplateId("b9e09d4c37961ba39e634cb1");
    }

    public PasswordResetSms(String password) {
        this();
        super.getPlaceholders().put("password", password);
    }

    public void setPassword(String password) {
        super.getPlaceholders().put("password", password);
    }
}
