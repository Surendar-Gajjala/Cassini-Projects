package com.cassinisys.irste.notification.sms;

import com.cassinisys.platform.service.notification.sms.SMSObject;

/**
 * Created by Nageshreddy on 21-11-2018.
 */
public class NewComplaintSMS extends SMSObject {

    public NewComplaintSMS() {
        super.setTemplateId("632290c40311030ec30a2323");
    }

    public NewComplaintSMS(String userid, String password) {
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
