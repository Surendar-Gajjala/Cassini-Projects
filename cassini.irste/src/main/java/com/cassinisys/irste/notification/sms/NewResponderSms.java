package com.cassinisys.irste.notification.sms;

import com.cassinisys.platform.service.notification.sms.SMSObject;

/**
 * Created by Nageshreddy on 21-11-2018.
 */
public class NewResponderSms extends SMSObject {


    public NewResponderSms() {
        super.setTemplateId("d905ac300a066d9c7ab944eb");
    }

    public NewResponderSms(String orderNumber, String customerName) {
        this();

        super.getPlaceholders().put("orderNumber", orderNumber);
        super.getPlaceholders().put("customerName", customerName);
    }

    public void setOrderNumber(String orderNumber) {
        super.getPlaceholders().put("orderNumber", orderNumber);
    }

    public void setCustomerName(String customerName) {
        super.getPlaceholders().put("customerName", customerName);
    }
}
