package com.cassinisys.platform.service.notification.sms;

/**
 * Created by reddy on 9/7/15.
 */
public class NewLoginAccountSms extends SMSObject {

    public NewLoginAccountSms() {
        super.setTemplateId("632290c40311030ec30a2323");
    }

    public NewLoginAccountSms(String userid, String password) {
        this();

        super.getPlaceholders().put("userid", userid);
        super.getPlaceholders().put("password", password);
    }

    public void setUserid(String userid) {
        super.getPlaceholders().put("userid", userid);
    }

    public void setPassword(String password) {
        super.getPlaceholders().put("password", password);
    }
}
